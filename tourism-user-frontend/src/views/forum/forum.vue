<template>
  <div class="forum">
    <headers></headers>
    <div class="forum-main">
      <div class="forum-search">
        <el-input
            size="small"
            class="forum-search-input"
            v-model="search.title"
            placeholder="请输入资讯"
            clearable
        ></el-input>
        <el-button
            size="small"
            type="primary"
            plain
            class="forum-search-btn"
            @click="searchPage"
        >搜索</el-button>
      </div>
      <div class="forum-list">
        <div
            class="forum-row"
            v-for="(item, index) in tableData"
            :key="item.id"
            @click="toInfo(item.id)"
        >
          <div class="forum-index">{{ index + 1 }}.</div>
          <div class="forum-title">{{ item.title }}</div>
        </div>
        <div v-if="!tableData.length" class="forum-empty">暂无资讯</div>
      </div>
      <el-pagination
          background
          :page-size="search.pageSize"
          layout="prev, pager, next"
          @current-change="handleCurrentChange"
          :total="total"
          class="forum-pagination"
      />
    </div>
    <bottoms></bottoms>
  </div>
</template>

<script>
import { getSysForumPage } from '../../api/api'
import headers from '@/components/header'
import bottoms from '@/components/bottom'
export default {
  data() {
    return {
      search: {
        title: "",
        pageSize: 12,
        pageNumber: 1,
      },
      total: 100,
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
      this.getSysForumPage()
    },
    getSysForumPage() {
      getSysForumPage(this.search).then(res => {
        if (res.code == 1000) {
          this.tableData = res.data.records
          this.total = res.data.total
        }
      })
    },
    toInfo(id) {
      this.$router.push("/forumInfo?id=" + id)
    },
    handleCurrentChange(val) {
      this.search.pageNumber = val
      this.getSysForumPage()
    },
  },
  mounted() {
    this.getSysForumPage()
  }
}
</script>

<style scoped>
.forum {
  width: 100%;
  min-height: 100vh;
  background: #f7f7f7;
  font-family: '黑体', 'Arial', sans-serif;
}
.forum-main {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.forum-search {
  margin-top: 24px;
  width: 80%;
  min-width: 300px;
  height: 80px;
  background: #fff;
  display: flex;
  align-items: center;
  border-radius: 16px;
  box-shadow: 0 2px 8px #f0f1f2;
  padding: 0 18px;
  gap: 18px;
}
.forum-search-input {
  width: 320px;
  margin-left: 20px;
}
.forum-search-btn {
  margin-left: 20px;
}
.forum-list {
  width: 80%;
  display: flex;
  flex-direction: column;
  gap: 22px;
  margin: 32px 0 0 0;
}
.forum-row {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 10px rgba(67,129,255,0.05);
  display: flex;
  align-items: center;
  padding: 18px 22px;
  gap: 14px;
  transition: box-shadow 0.2s;
  cursor: pointer;
}
.forum-row:hover {
  box-shadow: 0 6px 24px 0 rgba(67,129,255,0.13);
}
.forum-index {
  color: #888;
  font-size: 16px;
  min-width: 32px;
  text-align: right;
}
.forum-title {
  font-size: 17px;
  color: #222;
  font-weight: 500;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.forum-empty {
  padding: 80px 0;
  color: #aaa;
  text-align: center;
  font-size: 18px;
}
.forum-pagination {
  margin: 30px 0 18px 0;
  align-self: center;
}
@media (max-width: 650px) {
  .forum-search,
  .forum-list { width: 98%; }
  .forum-row { flex-direction: column; align-items: flex-start; padding: 14px 8px; gap: 6px;}
}
</style>