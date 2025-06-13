<template>
  <div class="line">
    <headers />
    <div class="line-main">
      <!-- 搜索栏 -->
      <div class="line-search-card">
        <el-input size="small" class="line-search-input" v-model="search.name" placeholder="请输入旅游线路名称" clearable />
        <el-input size="small" class="line-search-input" v-model="search.geography" placeholder="请输入地理情况" clearable />
        <el-input size="small" class="line-search-input" v-model="search.temperature" placeholder="请输入温度" clearable />
        <el-button size="small" type="primary" plain class="line-search-btn" @click="searchPage">搜索</el-button>
      </div>
      <!-- 列表 -->
      <div class="line-list">
        <div
            class="line-item"
            v-for="(item, index) in tableData"
            :key="item.id"
            @click="toInfo(item.id)"
            tabindex="0"
        >
          <div class="line-item-inner">
            <img class="line-img" :src="item.images.split(',')[0]" :alt="item.name" />
            <div class="line-info">
              <div class="line-title">{{ item.name }}</div>
              <div class="line-desc">{{ item.introduce }}</div>
              <div class="line-meta">
                <span v-if="item.geography">地理：{{ item.geography }}</span>
                <span v-if="item.temperature">温度：{{ item.temperature }}℃</span>
                <span v-if="item.days">天数：{{ item.days }}</span>
              </div>
            </div>
          </div>
        </div>
        <div v-if="!tableData.length" class="line-empty">暂无相关线路</div>
      </div>
      <!-- 分页 -->
      <el-pagination
          background
          :page-size="search.pageSize"
          layout="prev, pager, next"
          @current-change="handleCurrentChange"
          :total="total"
          class="line-pagination"
      />
    </div>
    <bottoms />
  </div>
</template>

<script>
import { getSysLinePage } from '../../api/api'
import headers from '@/components/header'
import bottoms from '@/components/bottom'
export default {
  data() {
    return {
      search: {
        name: "",
        geography: "",
        temperature: "",
        pageSize: 12,
        pageNumber: 1,
      },
      total: 0,
      tableData: [],
    }
  },
  components: {
    headers,
    bottoms
  },
  methods: {
    searchPage() {
      this.search.pageNumber = 1
      this.getSysLinePage()
    },
    getSysLinePage() {
      getSysLinePage(this.search).then(res => {
        if (res.code === 1000) {
          this.tableData = res.data.records
          this.total = res.data.total
        } else {
          this.tableData = []
          this.total = 0
        }
      })
    },
    toInfo(id) {
      // 可用于调试：console.log('跳转到线路详情', id)
      this.$router.push("/lineInfo?id=" + id)
    },
    handleCurrentChange(val) {
      this.search.pageNumber = val
      this.getSysLinePage()
    },
  },
  mounted() {
    this.getSysLinePage()
  }
}
</script>

<style scoped>
.line {
  width: 100%;
  min-height: 100vh;
  background: #f7f7f7;
  font-family: '黑体', 'Arial', sans-serif;
}
.line-main {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-bottom: 60px;
}
.line-search-card {
  display: flex;
  align-items: center;
  gap: 18px;
  width: 84vw;
  max-width: 980px;
  min-width: 320px;
  margin-top: 30px;
  margin-bottom: 12px;
  border-radius: 16px;
  box-shadow: 0 2px 8px #f0f1f2;
  background: #fff;
  padding: 18px 18px;
}
.line-search-input {
  width: 210px;
}
.line-search-btn {
  min-width: 80px;
}
.line-list {
  width: 84vw;
  max-width: 980px;
  display: flex;
  flex-direction: column;
  gap: 22px;
  margin: 16px 0 0 0;
}
.line-item {
  border-radius: 16px;
  transition: box-shadow 0.2s;
  cursor: pointer;
  padding: 0;
  background: #fff;
  box-shadow: 0 2px 8px #f0f1f2;
  margin-bottom: 8px;
  outline: none;
}
.line-item:hover,
.line-item:focus {
  box-shadow: 0 6px 24px 0 rgba(67,129,255,0.13);
}
.line-item-inner {
  display: flex;
  align-items: center;
  gap: 24px;
  padding: 20px;
}
.line-img {
  width: 200px;
  height: 140px;
  border-radius: 12px;
  object-fit: cover;
  background: #eee;
  flex-shrink: 0;
  box-shadow: 0 2px 8px 0 rgba(62,120,243,0.06);
}
.line-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.line-title {
  font-size: 20px;
  font-weight: bold;
  color: #234;
  margin-bottom: 4px;
  line-height: 1.2;
  word-break: break-all;
}
.line-desc {
  color: #444;
  font-size: 15px;
  line-height: 1.7;
  max-height: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
.line-meta {
  color: #888;
  font-size: 13px;
  margin-top: 8px;
  display: flex;
  gap: 18px;
}
.line-empty {
  color: #aaa;
  font-size: 18px;
  padding: 48px 0;
  text-align: center;
}
.line-pagination {
  margin: 28px 0 36px 0;
  align-self: center;
}
@media (max-width: 900px) {
  .line-search-card, .line-list { width: 98vw; max-width: 100%; }
  .line-img { width: 120px; height: 90px; }
  .line-item-inner { gap: 10px; padding: 10px;}
}
@media (max-width: 600px) {
  .line-search-card, .line-list { width: 99vw; padding: 6px 2px;}
  .line-item-inner {
    flex-direction: column;
    align-items: flex-start;
    padding: 10px 6px;
    gap: 10px;
  }
  .line-img { width: 98vw; max-width: 320px; height: 120px;}
}
</style>