<template>
  <div class="forumInfo">
    <headers />
    <div class="forumInfo-main">
      <el-card class="forumInfo-card">
        <el-carousel height="420px" class="forum-carousel" indicator-position="outside" arrow="always">
          <el-carousel-item v-for="(img, idx) in imageList" :key="idx">
            <img class="forum-carousel-img" :src="img" :alt="'图片'+(idx+1)" />
          </el-carousel-item>
        </el-carousel>
        <div class="forum-title-row">
          <span class="forum-title">{{ info.name }}</span>
          <el-button
              v-if="!flag"
              size="small"
              type="success"
              icon="el-icon-star-on"
              circle
              @click="saveSysFavor"
              class="favor-btn"
              title="收藏"
          ></el-button>
          <el-button
              v-if="flag"
              size="small"
              type="warning"
              icon="el-icon-star-off"
              circle
              @click="removeSysFavor"
              class="favor-btn"
              title="取消收藏"
          ></el-button>
        </div>
        <el-divider />
        <div class="forum-content" v-html="info.content"></div>
      </el-card>
    </div>
    <bottoms />
  </div>
</template>

<script>
import { getSysLineById, getSysFavor, saveSysFavor, removeSysFavor } from '../../api/api'
import headers from '@/components/header'
import bottoms from '@/components/bottom'

export default {
  data() {
    return {
      id: "",
      info: {
        content: "",
        name: "",
        images: ""
      },
      favor: {},
      flag: false,
    }
  },
  computed: {
    imageList() {
      if (this.info.images) {
        return this.info.images.split(',').filter(Boolean)
      }
      return []
    }
  },
  components: {
    headers,
    bottoms
  },
  methods: {
    removeSysFavor() {
      removeSysFavor({ ids: this.favor.id }).then(res => {
        if (res.code == 1000) {
          this.getSysFavor()
        }
      })
    },
    saveSysFavor() {
      var param = {
        lineId: this.id
      }
      saveSysFavor(param).then(res => {
        if (res.code == 1000) {
          this.$message({
            message: '收藏成功',
            type: 'success'
          });
          this.getSysFavor()
        }
      })
    },
    getSysLineById() {
      getSysLineById({ id: this.id }).then(res => {
        if (res.code == 1000) {
          this.info = res.data
        }
      })
    },
    getSysFavor() {
      getSysFavor({ id: this.id }).then(res => {
        if (res.code == 1000) {
          this.favor = res.data
          this.flag = true
        } else {
          this.flag = false
        }
      })
    },
  },
  mounted() {
    this.id = this.$route.query.id
    this.getSysFavor()
    this.getSysLineById()
  }
}
</script>

<style scoped>
.forumInfo {
  min-height: 100vh;
  background: #f7f7f7;
  font-family: '黑体', 'Arial', sans-serif;
}
.forumInfo-main {
  width: 100%;
  display: flex;
  justify-content: center;
  padding: 40px 0 60px 0;
  min-height: 80vh;
}

.forumInfo-card {
    width: 60vw;        /* 只占屏幕60% */
    max-width: 1100px;  /* 防止超大屏幕太宽 */
    min-width: 320px;
    min-height: 420px;
    border-radius: 18px;
    box-shadow: 0 4px 24px 0 rgba(62,120,243,0.13);
    background: #fff;
    padding: 0 0 38px 0;
    margin: 0 auto;
    overflow: hidden;
  }

.forum-carousel {
  border-radius: 16px;
  overflow: hidden;
  margin: 22px auto 12px auto;
  max-width: 1600px;
}
.forum-carousel-img {
  width: 100%;
  height: 420px;
  object-fit: cover;
  border-radius: 16px;
  background: #eee;
  display: block;
}
.forum-title-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 22px 48px 12px 48px;
  gap: 18px;
}
.forum-title {
  font-size: 2.6rem;
  font-weight: bold;
  color: #244;
  line-height: 1.2;
  word-break: break-word;
  flex: 1;
}
.favor-btn {
  margin-left: 16px;
}
.forum-content {
  font-size: 21px;
  color: #222;
  line-height: 2.2;
  word-break: break-word;
  min-height: 180px;
  margin: 0 48px;
  padding-bottom: 38px;
}
.el-divider {
  margin: 16px 48px;
}
::v-deep img {
  max-width: 100% !important;
  border-radius: 8px;
  margin: 12px 0;
  display: block;
}
@media (max-width: 1400px) {
  .forumInfo-card { max-width: 98vw; }
  .forum-carousel, .forum-carousel-img { height: 320px !important; }
  .forum-title { font-size: 2rem; }
  .forum-content { font-size: 17px; }
  .forum-title-row, .forum-content, .el-divider { margin-left: 24px; margin-right: 24px; }
}
@media (max-width: 900px) {
  .forumInfo-card { max-width: 99vw; }
  .forum-carousel, .forum-carousel-img { height: 210px !important; }
  .forum-title { font-size: 1.3rem; }
  .forum-content { font-size: 15px; }
  .forum-title-row, .forum-content, .el-divider { margin-left: 10px; margin-right: 10px; }
}
</style>