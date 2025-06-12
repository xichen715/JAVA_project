<template>
  <div class="attractionsInfo-page">
    <headers></headers>
    <div class="attractionsInfo-main">
      <div class="attractionsInfo-content">
        <!-- 图片轮播 -->
        <div class="attractionsInfo-carousel">
          <el-carousel height="360px">
            <el-carousel-item v-for="(item,index) in info.images.split(',')" :key="index">
              <img class="carousel-img" :src="item">
            </el-carousel-item>
          </el-carousel>
        </div>
        <!-- 右侧信息 -->
        <div class="attractionsInfo-right">
          <div class="attractionsInfo-title">{{info.name}}</div>
          <div class="attractionsInfo-meta">
            <span>门票价格：</span><b class="attractionsInfo-price">{{info.price}}</b> 元/人
            <span class="attractionsInfo-stock">库存：{{info.num}}</span>
          </div>
          <div class="attractionsInfo-intro">{{info.introduce}}</div>
          <div class="attractionsInfo-intro">预约须知：{{info.open}}</div>
          <div class="attractionsInfo-intro">景区开放时间：{{info.time}}</div>
          <div class="attractionsInfo-action">
            <el-button size="medium" type="primary" plain @click="toOrder">立即预约</el-button>
          </div>
        </div>
      </div>
    </div>
    <!-- 评论区 -->
    <div class="attractionsInfo-block">
      <div class="attractionsInfo-comment">
        <el-input class="comment-input" v-model="content" type="textarea" rows="5" placeholder="请输入评论内容"></el-input>
        <div class="comment-rate-row">
          <el-button class="comment-btn" type="primary" size="small" plain @click="saveSysComments">评论</el-button>
        </div>
        <div class="forum-list">
          <div class="forum-item" v-for="(item,index) in tableData" :key="index">
            <img class="forum-avatar" :src="$store.state.HOST + item.avatar">
            <div class="forum-main">
              <div class="forum-header">
                <span class="forum-user">{{item.createBy}}</span>
              </div>
              <div class="forum-content">{{item.content}}</div>
            </div>
          </div>
        </div>
        <el-pagination
            background
            :page-size="search.pageSize"
            layout="prev, pager, next"
            @current-change="handleCurrentChange"
            :total="total">
        </el-pagination>
      </div>
    </div>
    <!-- 预约弹窗 -->
    <el-dialog
        title="预约"
        :visible.sync="dialogVisible"
        width="40%">
      <span>
        <el-input-number size="small" v-model="num" :min="1" :max="10"></el-input-number>
        <el-date-picker size="small" style="margin-left:20px"
                        v-model="date1"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="选择预约日期">
        </el-date-picker>
        <div class="attractionsInfo9" v-for="(item,index) in people">
          <el-input size="small" v-model="item.name" placeholder="请输入姓名"></el-input>
          <el-input size="small" style="margin-left:10px" v-model="item.tel" placeholder="请输入电话"></el-input>
          <el-input size="small" v-if="info.realName == 1" style="margin-left:10px" v-model="item.idCard" placeholder="请输入身份证号"></el-input>
          <div v-if="index == (people.length - 1)" @click="addKeyword" class="plus-btn"><i class="el-icon-circle-plus-outline"></i></div>
          <div @click="minusKeyword(index)" v-if="index != 0" class="minus-btn"><i class="el-icon-remove-outline"></i></div>
        </div>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeOrder" size="small">取 消</el-button>
        <el-button type="primary" @click="saveOrder"  size="small">确 定</el-button>
      </span>
    </el-dialog>
    <bottoms></bottoms>
  </div>
</template>

<script>
import {getSysAttractionsById,saveSysAttractionOrder,getSysCommentsPage,saveSysComments} from '../../api/api'
import headers from '@/components/header'
import bottoms from '@/components/bottom'
export default {
  data() {
    return{
      id: "",
      dialogVisible: false,
      search: {
        attractionsId: "",
        pageSize: 10,
        pageNumber: 1,
      },
      info: {},
      total:100,
      tableData: [],
      content: "",
      score: null,
      num: "",
      userId: null,
      people: [
        {
          name: "",
          tel: "",
          idCard: ""
        }
      ],
    }
  },
  components: {
    headers,
    bottoms
  },
  methods: {
    saveSysComments() {
      if (!this.content) {
        this.$message({
          message: '请输入评论内容',
          type: 'warning'
        });
        return
      }
      var param = {
        content: this.content,
        score:this.score,
        attractionsId: this.id,
        userId:this.userId
      }
      saveSysComments(param).then(res => {
        if (res.code == 1000) {
          this.$message({
            message: '评论成功',
            type: 'success'
          });
          this.content = ""
          this.getSysCommentsPage()
        }
      })
    },
    getSysCommentsPage() {
      this.search.attractionsId = this.id
      getSysCommentsPage(this.search).then(res => {
        if (res.code == 1000) {
          this.tableData = res.data.records
          this.total = res.data.total
        }
      })
    },
    closeOrder() {
      this.date1 = ""
      this.num = ""
      this.people = [
        {
          name: "",
          tel: "",
          idCard: ""
        }
      ],
          this.dialogVisible = false
    },
    saveOrder() {
      if (!this.date1) {
        this.$message({
          message: '请选择预约时间',
          type: 'warning'
        });
        return
      }
      if (this.people.length < this.num) {
        this.$message({
          message: '请完善预约人信息',
          type: 'warning'
        });
        return
      }
      if (this.people.length > this.num) {
        this.$message({
          message: '预约人信息超出预约人数',
          type: 'warning'
        });
        return
      }
      for(let i = 0;i < this.people.length; i++) {
        var item = this.people[i]
        if (!item.name) {
          this.$message({
            message: '请完善预约人姓名',
            type: 'warning'
          });
          return
        }
        if (!item.tel) {
          this.$message({
            message: '请完善预约人联系方式',
            type: 'warning'
          });
          return
        }
        if (this.info.realName == 1&& !item.idCard ) {
          this.$message({
            message: '请完善预约人证件号',
            type: 'warning'
          });
          return
        }
      }
      var param = {
        attractionsId: this.id,
        num: this.num,
        time: this.date1,
        people: JSON.stringify(this.people)
      }
      saveSysAttractionOrder(param).then(res => {
        if (res.code == 1000) {
          this.$message({
            message: '预约成功，请等待确认',
            type: 'success'
          });
          this.closeOrder()
        } else {
          this.$message({
            message: res.message,
            type: 'warning'
          });
        }
      })
    },
    getSysAttractionsById() {
      getSysAttractionsById({id: this.id}).then(res => {
        if (res.code == 1000) {
          this.info = res.data
        }
      })
    },
    toOrder() {
      this.dialogVisible = true
    },
    addKeyword() {
      var param = {
        name: "",
        tel: "",
        idCard: ""
      }
      this.people.push(param)
    },
    minusKeyword(index) {
      this.people.splice(index,1)
    },
    handleCurrentChange() {

    }
  },
  mounted() {
    this.id = this.$route.query.id
    this.userId = JSON.parse(window.localStorage.getItem("user_info")).id
    this.getSysAttractionsById()
    this.getSysCommentsPage()
  }
}
</script>

<style scoped>
.attractionsInfo-page {
  background: #f6f8fb;
  min-height: 100vh;
  padding-bottom: 40px;
}
.attractionsInfo-main {
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 8px 32px 0 rgba(62,120,243,0.08);
  padding: 36px 38px 36px 38px;
  max-width: 1200px;
  margin: 50px auto;
}
.attractionsInfo-content {
  display: flex;
  gap: 38px;
  align-items: flex-start;
}
.attractionsInfo-carousel {
  flex: 1.2;
  min-width: 340px;
}
.carousel-img {
  width: 100%;
  height: 360px;
  object-fit: cover;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(67,129,255,0.07);
}
.attractionsInfo-right {
  flex: 1.8;
  display: flex;
  flex-direction: column;
  gap: 18px;
  justify-content: flex-start;
}
.attractionsInfo-title {
  font-size: 30px;
  font-weight: bold;
  color: #2a313c;
  margin-bottom: 6px;
  line-height: 1.2;
}
.attractionsInfo-meta {
  font-size: 16px;
  color: #666;
  margin-bottom: 5px;
  display: flex;
  align-items: center;
  gap: 16px;
}
.attractionsInfo-price {
  color: #fa9833;
  font-size: 20px;
  margin: 0 6px 0 3px;
  font-weight: bold;
}
.attractionsInfo-stock {
  font-size: 15px;
  color: #888;
  margin-left: 16px;
}
.attractionsInfo-intro {
  color: #444;
  font-size: 16px;
  line-height: 2;
  margin-bottom: 10px;
}
.attractionsInfo-action {
  margin-left: 35px; /* 让按钮和内容有空隙 */
  margin-right:10px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  min-width: 120px; /* 可根据需要调整 */
}
.attractionsInfo-block {
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 8px 32px 0 rgba(62,120,243,0.08);
  padding: 36px 38px 36px 38px;
  max-width: 1200px;
  margin: 50px auto;
}

.attractionsInfo-comment {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.comment-input {
  margin-bottom: 10px;
}
.comment-rate-row {
  margin-left: 35px; /* 让按钮和内容有空隙 */
  margin-right:10px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  min-width: 120px; /* 可根据需要调整 */
}
.comment-btn {
  margin-left: 10px;
}
.forum-list {
  margin: 10px 0 14px 0;
}
.forum-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 14px;
  padding: 12px 0;
  border-bottom: 1px solid #f2f2f2;
}
.forum-avatar {
  border-radius: 50%;
  width: 38px;
  height: 38px;
  object-fit: cover;
  margin-right: 12px;
}
.forum-main {
  flex: 1;
  display: flex;
  flex-direction: column;
}
.forum-header {
  color: #345faa;
  font-weight: 500;
  font-size: 16px;
  margin-bottom: 4px;
}
.forum-content {
  color: #444;
  font-size: 15px;
  line-height: 1.7;
}
.attractionsInfo9 {
  display: flex;
  align-items: center;
  margin-top: 15px;
}
.plus-btn, .minus-btn {
  color: #fa9833;
  font-size: 22px;
  cursor: pointer;
  margin-left: 8px;
  transition: color 0.18s;
}
.plus-btn:hover, .minus-btn:hover {
  color: #fa8000;
}
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  margin-top: 20px;
}
@media (max-width: 900px) {
  .attractionsInfo-main, .attractionsInfo-block {
    max-width: 97vw;
    padding: 16px 2vw;
  }
  .attractionsInfo-content { flex-direction: column; gap:18px;}
  .attractionsInfo-carousel, .attractionsInfo-right { width: 100%; }
  .carousel-img { height: 160px;}
}
</style>