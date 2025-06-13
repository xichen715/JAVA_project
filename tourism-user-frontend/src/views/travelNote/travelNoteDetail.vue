<template>
  <div class="travel-note-detail">
    <el-card v-if="note">
      <h2>{{ note.title }}</h2>
      <div class="meta">
        <span>作者ID：{{ note.userId }}</span>
        <span style="margin-left:20px">标签：{{ note.tag }}</span>
        <span style="margin-left:20px">创建时间：{{ note.createTime }}</span>
      </div>
      <div style="margin: 16px 0">
        <img v-if="note.coverImg" :src="note.coverImg" style="max-width:300px" />
      </div>
      <div class="content" v-html="note.content"></div>
      <div class="stat" style="margin-top:20px;">
        <span>浏览量：{{ note.viewCount }}</span>
        <span style="margin-left: 20px;">点赞数：{{ note.likeCount }}</span>
      </div>
      <el-button style="margin-top:20px;" @click="$router.go(-1)">返回</el-button>
    </el-card>
    <el-empty v-else description="游记不存在" />
  </div>
</template>

<script>
import { getTravelNoteById } from "@/api/api"

export default {
  name: 'TravelNoteDetail',
  data() {
    return {
      note: null
    }
  },
  created() {
    const id = this.$route.params.id
    if (id) {
      getTravelNoteById(id).then(res => {
        this.note = res.data
      })
    }
  }
}
</script>