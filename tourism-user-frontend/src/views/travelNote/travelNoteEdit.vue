<template>
  <div class="travel-note-edit">
    <el-card>
      <el-form :model="form" ref="form" label-width="80px">
        <el-form-item label="标题" required>
          <el-input v-model="form.title" autocomplete="off" />
        </el-form-item>
        <el-form-item label="标签">
          <el-input v-model="form.tag" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="封面">
          <el-upload
              class="cover-uploader"
              :action="uploadCoverUrl()"
              :headers="uploadHeaders"
              :show-file-list="false"
              :on-success="handleCoverSuccess"
              :before-upload="beforeCoverUpload"
              accept="image/*"
          >
            <img v-if="form.coverImg" :src="form.coverImg" class="cover-img" style="max-width:100px"/>
            <i v-else class="el-icon-plus cover-uploader-icon" style="font-size:32px;cursor:pointer;"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="正文" required>
          <el-input
              type="textarea"
              v-model="form.content"
              :rows="8"
              placeholder="请输入正文内容"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">保存</el-button>
          <el-button @click="onCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { Message } from 'element-ui'
import { getTravelNoteById, addTravelNote, updateTravelNote } from "@/api/api"

export default {
  name: 'TravelNoteEdit',
  data() {
    return {
      form: {
        id: '',
        title: '',
        tag: '',
        content: '',
        coverImg: ''
      }
    }
  },
  computed: {
    uploadHeaders() {
      // 假设你的token存储在localStorage，key为'token'
      const token = localStorage.getItem('token')
      return token ? { Authorization: token } : {}
      // 如果后端需要 'Bearer ' 前缀，则 return token ? { Authorization: 'Bearer ' + token } : {}
    }
  },
  created() {
    const id = this.$route.params.id
    if (id) {
      getTravelNoteById(id).then(res => {
        this.form = res.data
      })
    }
  },
  methods: {
    uploadCoverUrl() {
      return this.$store.state.HOST + '/travelNote/uploadCover'
    },
    beforeCoverUpload(file) {
      const isImage = file.type.startsWith('image/')
      if (!isImage) {
        Message.error('只能上传图片格式')
      }
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) {
        Message.error('图片大小不能超过 2MB!')
      }
      return isImage && isLt2M
    },
    handleCoverSuccess(res) {
      if (res.code === 1000 && res.url) {
        // 判断是不是完整url
        if (/^https?:\/\//.test(res.url)) {
          this.form.coverImg = res.url
        } else {
          this.form.coverImg = this.$store.state.HOST + res.url
        }
        Message.success('封面上传成功')
      } else {
        Message.error(res.msg || '封面上传失败')
      }
    },
    onSubmit() {
      if (!this.form.title || !this.form.content) {
        Message.warning('标题和正文不能为空')
        return
      }
      if (this.form.id) {
        updateTravelNote(this.form).then(() => {
          Message.success('修改成功')
          this.$router.push({ name: 'travelNote' })
        })
      } else {
        addTravelNote(this.form).then(() => {
          Message.success('发布成功')
          this.$router.push({ name: 'travelNote' })
        })
      }
    },
    onCancel() {
      this.$router.go(-1)
    }
  }
}
</script>