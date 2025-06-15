<template>
  <div class="common-layout">
    <el-container>
      <el-asside></el-asside>
      <el-container>
        <el-header></el-header>

        <!-- 标签页导航栏 -->
        <div class="main-menu">
          <div class="scroll-left" @click="leftScroll">
            <i class="el-icon-arrow-left"></i>
          </div>

          <div class="menu" id="menu">
            <div
                class="menu-item"
                v-for="(item, index) in activeMenuArrary"
                :key="index"
                @click="openMenu(item.url)"
            >
              <span
                  class="menu-name"
                  :class="{ active: item.url === activeMenu }"
              >
                {{ item.name }}
              </span>
              <i
                  v-if="item.url !== '/index'"
                  class="menu-icon el-icon-close"
                  @click.stop="closeMenu(item.url)"
              ></i>
            </div>
          </div>

          <div class="scroll-right" @click="rightScroll">
            <i class="el-icon-arrow-right"></i>
          </div>
        </div>

        <el-main></el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import elAsside from '@/components/layout/aside'
import elMain from '@/components/layout/main'
import elHeader from '@/components/layout/header'

export default {
  name: 'index',
  components: {
    elAsside,
    elMain,
    elHeader
  },
  computed: {
    ...mapState({
      activeMenuArrary: state => state.menu.activeMenuArrary,
      activeMenu: state => state.menu.activeMenu
    })
  },
  methods: {
    leftScroll() {
      const container = document.getElementById('menu')
      container.scrollLeft -= 100
    },
    rightScroll() {
      const container = document.getElementById('menu')
      container.scrollLeft += 100
    },
    openMenu(url) {
      if (this.activeMenu !== url) {
        this.$router.push({ path: url })
        this.$store.commit('menu/setActiveMenu', url)
      }
    },
    closeMenu(url) {
      const index = this.activeMenuArrary.findIndex(item => item.url === url)

      if (this.activeMenu === url && index > 0) {
        const prevUrl = this.activeMenuArrary[index - 1].url
        this.$router.push({ path: prevUrl })
        this.$store.commit('menu/setActiveMenu', prevUrl)
      }

      this.$store.commit('menu/reduceActiveMenu', index)
      this.$bus.$emit('clearKeepAlive', url)
    }
  }
}
</script>

<style scoped>
.common-layout >>> .el-menu-item {
  min-width: 0;
}

.main-menu {
  width: 100%;
  height: 40px;
  border-top: 1px solid #ececf1;
  background-color: #f9f9fb;
  display: flex;
  align-items: center;
  font-family: "Helvetica Neue", "黑体", sans-serif;
  box-shadow: inset 0 -1px 0 #e0e0e0;
}

.menu {
  display: flex;
  align-items: center;
  height: 100%;
  width: calc(100% - 60px);
  overflow-x: auto;
  white-space: nowrap;
  scrollbar-width: none;
}
.menu::-webkit-scrollbar {
  display: none;
}

.scroll-left,
.scroll-right {
  width: 30px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: #999;
  transition: background-color 0.2s;
}
.scroll-left:hover,
.scroll-right:hover {
  background-color: #eaeaea;
  color: #333;
}

.menu-item {
  height: 100%;
  display: flex;
  align-items: center;
  padding: 0 10px;
  border-right: 1px solid #e0e0e0;
  background-color: #f5f7fa;
  transition: background-color 0.3s;
  flex-shrink: 0;
  max-width: 160px;
}
.menu-item:hover {
  background-color: #e8f0fe;
}

.menu-name {
  font-size: 13px;
  color: #444;
  max-width: 110px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.menu-name.active {
  color: #3e78f3;
  font-weight: bold;
  border-bottom: 2px solid #3e78f3;
  padding-bottom: 2px;
}

.menu-icon {
  margin-left: 8px;
  font-size: 12px;
  color: #999;
  transition: color 0.2s;
}
.menu-icon:hover {
  color: #f56c6c;
}
</style>
