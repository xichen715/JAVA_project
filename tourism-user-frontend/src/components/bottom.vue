<template>
  <footer class="footer">
    <div class="footer-main">
      <div class="footer-brand">
        <div class="footer-title">
          <img src="@/assets/image/logo.png" alt="JoyPath Logo" class="footer-logo" />
          <span>JoyPath 旅行网</span>
        </div>
        <div class="footer-desc">
          JoyPath，让旅行更简单。<br>为旅行者提供一站式的景点购票、酒店预订、攻略查询、行程规划等服务。
        </div>
      </div>
      <div class="footer-links">
        <div class="footer-links-title">快速导航</div>
        <div class="footer-nav">
          <span v-for="item in navList" :key="item.name" @click="toPage(item.name)">
            {{ item.label }}
          </span>
        </div>
      </div>
      <div class="footer-contact">
        <div class="footer-links-title">联系我们</div>
        <div class="footer-contact-item">Email: <a href="mailto:ethanwang@example.com">ethanwang@example.com</a></div>
        <div class="footer-contact-item">Email: <a href="mailto:baichengchi@example.com">baichengchi@example.com</a></div>
        <div class="footer-contact-item">GitHub: <a href="https://github.com/xichen715" target="_blank">xichen715</a></div>
      </div>
    </div>
    <div class="footer-divider"></div>
    <div class="footer-copyright">
      <span>© 2025 JoyPath 旅行网 | 由 <span class="footer-author">EthanWANG</span> 和 <span class="footer-author">BaichengCHI</span> 开发</span>
      <span>本项目为北航2025春JAVA企业级开发大作业。</span>
    </div>
  </footer>
</template>

<script>
export default {
  name: "BottomPage",
  data() {
    return {
      navList: [
        { name: "index", label: "主页" },
        { name: "attractions", label: "景点" },
        { name: "hotel", label: "酒店" },
        { name: "line", label: "旅行攻略" },
        { name: "forum", label: "相关资讯" },
        { name: "itinerary", label: "行程规划" },
        { name: "center", label: "个人中心" },
      ]
    }
  },
  methods: {
    toPage(name) {
      if (name === "manage") {
        window.open("http://localhost:3000")
      } else {
        this.$router.push(
            this.navList.find(i => i.name === name)?.name === "index" ? "/" : `/${name}`
        )
      }
    }
  }
}
</script>

<style scoped>
.footer {
  width: 100vw;
  background: #f6f8fa; /* 浅色背景 */
  color: #222;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", Arial, sans-serif;
  padding-top: 36px;
  box-shadow: 0 -2px 12px 0 rgba(0,0,0,0.04);
}
.footer-main {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  gap: 36px;
  flex-wrap: wrap;
  padding: 0 3vw 16px 3vw;
}
.footer-brand {
  flex: 2.2;
  min-width: 260px;
}
.footer-title {
  display: flex;
  align-items: center;
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 10px;
  gap: 12px;
  color: #2070f3;
}
.footer-logo {
  height: 38px;
  width: 38px;
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 2px 6px 0 rgba(32,112,243,0.06);
}
.footer-desc {
  font-size: 15px;
  color: #555;
  margin-bottom: 16px;
  line-height: 1.7;
}
.footer-authors {
  font-size: 15px;
  margin-top: 10px;
  color: #ffb300;
}
.footer-author {
  color: #e67c17;
  font-weight: bold;
}
.footer-links, .footer-contact {
  flex: 1.1;
  min-width: 200px;
  margin-top: 10px;
}
.footer-links-title {
  color: #2070f3;
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
  letter-spacing: 1px;
}
.footer-nav {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.footer-nav span {
  font-size: 15px;
  color: #444;
  cursor: pointer;
  transition: color 0.18s, background 0.18s;
  border-radius: 3px;
  padding: 2px 0 2px 6px;
}
.footer-nav span:hover {
  color: #fff;
  background: #2070f3;
}
.footer-contact-item {
  font-size: 15px;
  margin-bottom: 7px;
  color: #555;
}
.footer-contact-item a {
  color: #2070f3;
  text-decoration: underline;
  transition: color 0.18s;
}
.footer-contact-item a:hover {
  color: #e67c17;
}
.footer-divider {
  border-top: 1px solid #e0e0e0;
  width: 92vw;
  margin: 24px auto 0 auto;
}
.footer-copyright {
  width: 100vw;
  padding: 12px 0 20px 0;
  background: #f2f4f8;
  text-align: center;
  font-size: 14px;
  color: #888;
  display: flex;
  flex-direction: column;
  gap: 3px;
  border-top: 1px solid #e0e0e0;
}
@media (max-width: 900px) {
  .footer-main {
    flex-direction: column;
    gap: 18px;
    padding: 0 2vw 10px 2vw;
  }
  .footer-brand, .footer-links, .footer-contact {
    min-width: unset;
  }
  .footer-logo {
    height: 30px;
    width: 30px;
  }
  .footer-divider {
    width: 98vw;
    margin-top: 16px;
  }
}
</style>