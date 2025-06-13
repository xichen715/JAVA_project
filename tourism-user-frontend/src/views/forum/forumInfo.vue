<template>
  <div class="forumInfo">
    <headers />
    <div class="forumInfo-main">
      <el-card class="forumInfo-card">
        <h2 class="forumInfo-title">{{ info.title }}</h2>
        <div class="forumInfo-meta">
          <span v-if="info.author" class="forumInfo-author">作者：{{ info.author }}</span>
          <span v-if="info.createTime" class="forumInfo-date">{{ info.createTime | formatDate }}</span>
        </div>
        <el-divider />
        <div class="forumInfo-content" v-html="info.content"></div>
      </el-card>
    </div>
    <bottoms />
  </div>
</template>

<script>
import { getSysForumById } from '../../api/api'
import headers from '@/components/header'
import bottoms from '@/components/bottom'
import { format } from 'date-fns'
export default {
  data() {
    return {
      id: "",
      info: {},
    }
  },
  components: {
    headers,
    bottoms
  },
  filters: {
    formatDate(val) {
      if (!val) return ''
      return format(new Date(val), 'yyyy-MM-dd HH:mm')
    }
  },
  methods: {
    getSysForumById() {
      getSysForumById({ id: this.id }).then(res => {
        if (res.code === 1000) {
          this.info = res.data
        }
      })
    },
  },
  mounted() {
    this.id = this.$route.query.id
    this.getSysForumById()
  }
}
</script>

<style scoped>
.forumInfo {
  width: 100%;
  min-height: 100vh;
  background: #f7f7f7;
  font-family: '黑体', 'Arial', sans-serif;
}
.forumInfo-main {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: 32px 0 60px 0;
  min-height: 65vh;
}
.forumInfo-card {
  width: 98vw;
  max-width: 780px;
  min-height: 350px;
  border-radius: 16px;
  box-shadow: 0 2px 16px 0 rgba(62,120,243,0.13);
  background: #fff;
  padding: 36px 32px 32px 32px;
  margin: 0 auto;
}
.forumInfo-title {
  font-size: 2rem;
  font-weight: bold;
  color: #234;
  margin-bottom: 10px;
  line-height: 1.2;
  word-break: break-all;
}
.forumInfo-meta {
  font-size: 14px;
  color: #888;
  margin-bottom: 12px;
  display: flex;
  gap: 30px;
}
.forumInfo-content {
  font-size: 16px;
  color: #222;
  line-height: 2;
  word-break: break-word;
  min-height: 160px;
}
::v-deep .forumInfo-content img {
  max-width: 100% !important;
  border-radius: 8px;
  margin: 12px 0;
  display: block;
}
@media (max-width: 800px) {
  .forumInfo-card {
    max-width: 98vw;
    padding: 18px 8px;
  }
  .forumInfo-title {
    font-size: 1.2rem;
  }
}
</style>