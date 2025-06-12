<template>
  <div class="index">
    <headers></headers>
    <el-carousel height="500px">
      <el-carousel-item v-for="(item,index) in rotations" :key="index">
        <img style="width:100%;height:100%" :src="$store.state.HOST + item.image">
      </el-carousel-item>
    </el-carousel>
    <div class="index1">
      <div class="index2">
        <div class="index3">
          推荐景点
        </div>
        <div class="index4">
          <div class="index5" v-for="(item,index) in attractions" :key="index" >
            <img style="width:100%;height:220px" :src="item.images.split(',')[0]">
            <div class="index6">
              <div class="index7">{{item.name}}</div>
              <div class="index8">{{item.introduce}}</div>
            </div>
            <div class="index9" style="margin-top:10px" @click="toInfo(item.id)">
                查看详情
              </div>
          </div>
        </div>
        <div class="btn-right">
          <el-button size="small" type="primary" plain @click="toAttraction">探索更多景点</el-button>
        </div>
      </div>
    </div>
    <div class="index10">
    </div>
    <div class="index1">
      <div class="index2">
        <div class="index3">
          精选游记
        </div>
        <div class="index4">
          <div class="index5" v-for="(item,index) in line">
            <img style="width:100%;height:220px" :src="item.images.split(',')[0]">
            <div class="index6">
              <div class="index7">{{item.name}}</div>
              <div class="index8">{{item.introduce}}</div>
              <div class="index9" @click="toLineInfo(item.id)">
                查 看
              </div>
            </div>
          </div>
        </div>
        <div class="btn-right">
          <el-button size="small" type="primary" plain @click="toLine">探索更多游记</el-button>
        </div>
      </div>
    </div>
    <bottoms></bottoms>
  </div>
</template>

<script>
  import {getSysRotationsList,getSysAttractionsIndex,getUserCount,getSysLineIndex} from '../../api/api'
  import headers from '@/components/header'
  import bottoms from '@/components/bottom'
  export default {
    data() {
      return{
        rotations: [],
        attractions: [],
        line: [],
      }
    },
    components: {
      headers,
      bottoms
    },
    methods: {
      toAttraction() {
        this.$router.push("/attractions")
      },
      toLine() {
        this.$router.push("/line")
      },
      toInfo(id) {
        this.$router.push("/attractionsInfo?id=" + id)
      },
      toLineInfo(id) {
        this.$router.push("/lineInfo?id=" + id)
      },
      getSysRotationsList() {
        getSysRotationsList().then(res => {
          if (res.code == 1000) {
            this.rotations = res.data
          }
        })
      },
      getSysAttractionsIndex() {
        getSysAttractionsIndex().then(res => {
          if (res.code == 1000) {
            this.attractions = res.data
          }
        })
      },
      getUserCount() {
        getUserCount().then(res => {
          if (res.code == 1000) {
            this.count = res.data
          }
        })
      },
      getSysLineIndex() {
        getSysLineIndex().then(res => {
          if (res.code == 1000) {
            this.line = res.data
          }
        })
      }
    },
    created() {
     
    },
    mounted() {
      this.getSysRotationsList()
      this.getSysAttractionsIndex()
      this.getUserCount()
      this.getSysLineIndex()
    }
 }
</script>

<style scoped>
   @import url('../../assets/css/index.css');
</style>