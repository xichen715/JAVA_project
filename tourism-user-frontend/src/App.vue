<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<style>
html, body, #app {
  margin: 0;
  padding: 0;
  min-height: 100vh;
  font-family: "PingFang SC", "黑体", Arial, sans-serif;
  background: #f7f7f7;
}

/* 优雅的滚动条样式 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
  background: #f0f0f0;
  border-radius: 6px;
}
::-webkit-scrollbar-thumb {
  background: #c0c4cc;
  border-radius: 6px;
}

/* Element Plus 分页器小调整 */
.el-pagination {
  padding: 16px 0 24px 0 !important;
  display: flex;
  justify-content: flex-end;
}

/* Element Plus 对话框圆角、标题 */
.el-dialog {
  border-radius: 10px;
}
.el-dialog__title {
  font-size: 15px !important;
  font-family: "黑体";
}
.el-dialog__header {
  border-bottom: 1px solid #e3e8ee;
}
</style>
