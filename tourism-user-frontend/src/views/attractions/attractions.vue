<template>
  <div class="attractions">
    <headers></headers>
    <div class="attractions1">
      <div class="attractions2">
        <el-input size="small" style="width:300px;margin-left:20px" v-model="search.name" placeholder="请输入景点名称"></el-input>
        <el-button size="small" style="margin-left:20px" type="primary" plain @click="searchPage">搜索</el-button>
      </div>
      <div class="attractions3">
        <div class="attraction-row" v-for="(item,index) in tableData" :key="index">
          <img class="attraction-image" :src="item.images.split(',')[0]" />
          <div class="attraction-info">
            <div class="attraction-name">{{item.name}}</div>
            <div class="attraction-desc">{{item.introduce}}</div>
            <div class="attraction-action">
              <el-button type="primary" plain size="small" @click="toInfo(item.id)">查看详情</el-button>
            </div>
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
    <bottoms></bottoms>
  </div>
</template>

<script>
import {getSysAttractionsPage} from '../../api/api'
import headers from '@/components/header'
import bottoms from '@/components/bottom'
export default {
  data() {
    return{
      search: {
        name: "",
        state: 1,
        pageSize: 12,
        pageNumber: 1,
      },
      total:100,
      tableData: [],
    }
  },
  components: {
    headers,
    bottoms
  },
  methods: {
    searchPage() {
      this.pageNumber = 1
      this.getSysAttractionsPage()
    },
    getSysAttractionsPage() {
      getSysAttractionsPage(this.search).then(res => {
        if (res.code == 1000) {
          this.tableData = res.data.records
          this.total = res.data.total
        }
      })
    },
    toInfo(id) {
      this.$router.push("/attractionsInfo?id=" + id)
    },
    handleCurrentChange(val) {
      this.search.pageNumber = val
      this.getSysAttractionsPage()
    },
  },
  mounted() {
    this.getSysAttractionsPage()
  }
}
</script>

<style scoped>
.attractions {
  width: 100%;
  min-height: 100vh;
  font-family: '黑体', 'Arial', sans-serif;
  background: #f7f7f7;
}
.attractions1 {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.attractions2 {
  margin-top: 20px;
  width: 80%;
  min-width: 300px;
  height: 80px;
  background-color: #fff;
  display: flex;
  align-items: center;
  border-radius: 16px;
}
.attractions3 {
  width: 80%;
  display: flex;
  flex-direction: column;
  gap: 24px;
  margin: 30px 0;
}
.attraction-row {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 10px rgba(67,129,255,0.05);
  display: flex;
  align-items: center;
  padding: 20px 22px;
  gap: 28px;
  transition: box-shadow 0.2s;
  cursor: pointer;
}
.attraction-row:hover {
  box-shadow: 0 6px 24px 0 rgba(67,129,255,0.13);
}
.attraction-image {
  width: 210px;
  height: 150px;
  border-radius: 12px;
  object-fit: cover;
  margin-right: 18px;
  flex-shrink: 0;
  background: #eee;
}
.attraction-info {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-width: 0;
}
.attraction-name {
  font-size: 21px;
  font-weight: bold;
  color: #222;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.attraction-desc {
  color: #666;
  font-size: 15px;
  line-height: 1.7;
  margin-bottom: 15px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2; /* 最多显示2行 */
  -webkit-box-orient: vertical;
}
.attraction-action {
  margin-left: 35px; /* 让按钮和内容有空隙 */
  margin-right:10px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  min-width: 120px; /* 可根据需要调整 */
}
.el-pagination {
  margin: 30px 0 18px 0;
}
@media (max-width: 650px) {
  .attractions2, .attractions3 { width: 98%; }
  .attraction-row { flex-direction: column; align-items: flex-start; padding: 14px 8px; gap: 10px;}
  .attraction-image { width: 100%; height: 150px; margin-right: 0; margin-bottom: 10px;}
}
</style>