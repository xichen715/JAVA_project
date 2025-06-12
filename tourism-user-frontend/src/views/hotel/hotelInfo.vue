<template>
  <div class="hotel-detail-page">
    <headers />
    <div class="hotel-detail-container">
      <!-- 信息头部 -->
      <div class="hotel-header">
        <div>
          <div class="hotel-title-row">
            <span class="hotel-title">{{ info.name }}</span>
          </div>
          <div class="hotel-meta">
            <span class="hotel-scenic">所属景点：{{ info.attractions }}</span>
            <span class="hotel-address">
              地址：{{ info.address }}
              <el-button type="text" icon="el-icon-location-outline" @click="openMap" class="map-btn">查看地图</el-button>
            </span>
          </div>
        </div>
      </div>
      <!-- 图片区 -->
      <div class="hotel-gallery">
        <div class="main-image-box">
          <img :src="mainImage" class="main-image" />
          <div class="img-count">{{ images.length }} 张图片</div>
        </div>
        <div class="thumb-list">
          <img
              v-for="(img, i) in images.slice(0, 6)"
              :key="i"
              :src="img"
              class="thumb-img"
              :class="{ active: mainImage === img }"
              @click="mainImage = img"
          />
        </div>
      </div>
      <!-- 简介 -->
      <div class="hotel-desc-row">
        <div class="desc-text">{{ info.introduce }}</div>
        <el-button type="primary" class="book-btn" size="medium" @click="toOrder">立即预定</el-button>
      </div>
    </div>
    <el-dialog
        title="预定"
        :visible.sync="dialogVisible"
        width="400px"
        :modal-append-to-body="true"
    >
      <div class="order-dialog">
        <div class="order-radio">
          <el-radio-group v-model="radio1">
            <el-radio
                v-for="(item,index) in hotel"
                :key="index"
                :label="item.id"
                border
            >{{item.name}} - 数量{{item.num}} - 价格{{item.price}}</el-radio>
          </el-radio-group>
        </div>
        <div class="order-row">
          <el-input-number v-model="num" :min="1" :max="10" size="small" style="width:110px"/>
          <el-date-picker
              style="margin-left:16px;width:150px"
              v-model="date1"
              type="date"
              value-format="yyyy-MM-dd"
              size="small"
              placeholder="选择预定日期"/>
        </div>
        <div class="order-row" style="margin-top:10px">
          <el-input size="small" v-model="people.name" placeholder="请输入姓名" style="width:110px"/>
          <el-input size="small" v-model="people.tel" placeholder="请输入电话" style="width:110px;margin-left:10px"/>
          <el-input size="small" v-model="people.idCard" placeholder="请输入身份证号" style="width:160px;margin-left:10px"/>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="closeOrder" size="small">取 消</el-button>
    <el-button type="primary" @click="saveOrder" size="small">确 定</el-button>
  </span>
    </el-dialog>
    <bottoms />
  </div>
</template>

<script>
import headers from '@/components/header'
import bottoms from '@/components/bottom'
import { getSysHotelById, getSysHotelItemList, saveSysHotelOrder } from '../../api/api'

export default {
  data() {
    return {
      id: "",
      dialogVisible: false,
      date1: "",
      num: "",
      people: {
        name: "",
        tel: "",
        idCard: ""
      },
      info: {},
      hotel: [],
      radio1: "",
      images: [],
      mainImage: ""
    }
  },
  components: {
    headers,
    bottoms
  },
  methods: {
    getSysHotelItemList() {
      getSysHotelItemList({id:this.id}).then(res => {
        if (res.code == 1000) {
          this.hotel = res.data
        }
      })
    },
    closeOrder() {
      this.date1 = ""
      this.num = ""
      this.radio1 = ''
      this.people = {
        name: "",
        tel: "",
        idCard: ""
      }
      this.dialogVisible = false
    },
    saveOrder() {
      if (!this.date1) {
        this.$message({ message: '请选择预约时间', type: 'warning' }); return
      }
      if (!this.people.name) {
        this.$message({ message: '请完善预约人姓名', type: 'warning' }); return
      }
      if (!this.people.tel) {
        this.$message({ message: '请完善预约人联系方式', type: 'warning' }); return
      }
      if (!this.people.idCard ) {
        this.$message({ message: '请完善预约人证件号', type: 'warning' }); return
      }
      if (!this.radio1 ) {
        this.$message({ message: '请选择房型', type: 'warning' }); return
      }
      var param = {
        hotelId: this.id,
        num: this.num,
        itemId: this.radio1,
        time: this.date1,
        people: JSON.stringify(this.people)
      }
      saveSysHotelOrder(param).then(res => {
        if (res.code == 1000) {
          this.$message({ message: '预约成功，请等待确认', type: 'success' });
          this.closeOrder()
        } else {
          this.$message({ message: res.message, type: 'warning' });
        }
      })
    },
    getSysHotelById() {
      getSysHotelById({id:this.id}).then(res => {
        if (res.code == 1000) {
          this.info = res.data
          // 新增: 解析图片数组
          this.images = res.data.images ? res.data.images.split(',') : []
          this.mainImage = this.images[0] || ''
        }
      })
    },
    toOrder() {
      this.dialogVisible = true
    },
    openMap() {
      const keyword = encodeURIComponent((this.info.name || '') + ' ' + (this.info.address || ''));
      window.open(`https://www.amap.com/search?query=${keyword}`);
    }
  },
  mounted() {
    this.id = this.$route.query.id
    this.getSysHotelById()
    this.getSysHotelItemList()
  }
}
</script>

<style scoped>
.hotel-detail-page {
  background: #f6f8fb;
  min-height: 100vh;
}
.hotel-detail-container {
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 8px 32px 0 rgba(62,120,243,0.08);
  padding: 36px 38px 36px 38px;
  max-width: 1200px;
  margin: 50px auto;
}
.hotel-header {
  margin-bottom: 18px;
}
.hotel-title-row {
  font-size: 0;
  display: flex;
  align-items: center;
  gap: 14px;
}
.hotel-title {
  font-size: 32px;
  font-weight: bold;
  color: #222;
  margin-right: 12px;
  display: inline-block;
  line-height: 1.1;
}

.hotel-meta {
  font-size: 15px;
  color: #666;
  margin-top: 7px;
  display: flex;
  align-items: center;
  gap: 22px;
}
.hotel-scenic {
  background: #ffeec6;
  color: #b58900;
  border-radius: 5px;
  padding: 2px 10px;
  font-size: 14px;
}
.hotel-address .map-btn {
  color: #fa9833;
  font-size: 15px;
  margin-left: 10px;
  padding: 0;
}
.hotel-gallery {
  display: flex;
  gap: 26px;
  margin-bottom: 26px;
}
.main-image-box {
  position: relative;
  width: 59%;
}
.main-image {
  width: 100%;
  height: 340px;
  object-fit: cover;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(67,129,255,0.07);
}
.img-count {
  position: absolute;
  right: 18px;
  top: 18px;
  background: rgba(40,40,40,0.7);
  color: #fff;
  font-size: 17px;
  border-radius: 7px;
  padding: 3px 13px;
}
.thumb-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  width: 180px;
}
.thumb-img {
  width: 100%;
  height: 68px;
  object-fit: cover;
  border-radius: 7px;
  cursor: pointer;
  border: 2px solid #fff;
  transition: border .15s;
}
.thumb-img.active,
.thumb-img:hover {
  border: 2px solid #fa9833;
}
.hotel-desc-row {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  border-top: 1px solid #f2f2f2;
  padding-top: 22px;
  margin-top: 18px;
}
.desc-text {
  font-size: 16px;
  color: #444;
  line-height: 1.9;
  flex: 1;
  margin-right: 24px;
}
.book-btn {
  min-width: 120px;
  font-size: 17px;
  font-weight: bold;
  border-radius: 19px;
  height: 44px;
  box-shadow: 0 2px 8px rgba(250,152,51,0.08);
}
.order-dialog {
  display: flex;
  flex-direction: column;
  gap: 18px;
  margin-top: 10px;
}
.order-radio {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.order-radio .el-radio {
  margin-bottom: 8px;
  min-width: 320px;
}
.order-row {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 2px;
}
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  margin-top: 20px;
}
@media (max-width:900px) {
  .hotel-detail-container { padding: 14px 2vw;}
  .hotel-header, .hotel-gallery, .hotel-desc-row { flex-direction: column; align-items: flex-start;}
  .main-image-box, .thumb-list { width: 100%; }
  .main-image { height: 180px;}
  .thumb-img { height: 44px;}
  .desc-text { margin-right: 0;}
  .hotel-desc-row { align-items: flex-start;}
  .book-btn { margin-top: 18px;}
}
</style>