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
            <span class="hotel-scenic">所属景区：{{ info.attractions }}</span>
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
    <!-- 预定弹窗 -->
    <el-dialog
        title="预定"
        :visible.sync="dialogVisible"
        width="400px"
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
      if (this.info.address) {
        window.open(`https://map.baidu.com/search/${encodeURIComponent(this.info.address)}`)
      }
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
   @import url('../../assets/css/hotelinfo.css');
</style>