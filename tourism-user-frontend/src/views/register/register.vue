<template>
  <div class="login-bg">
    <div class="login-center">
      <div class="login-box">
        <div class="login-logo-row">
          <img src="../../assets/image/logo.png" class="logo-img" alt="logo" />
          <span class="login-title">欢迎来到JoyPath乐途</span>
        </div>
        <el-input prefix-icon="el-icon-user" v-model="loginAccount" placeholder="请输入登录账号" class="login-input"></el-input>
        <el-input prefix-icon="el-icon-star-off" type="password" v-model="password" placeholder="请输入用户密码" class="login-input"></el-input>
        <el-input prefix-icon="el-icon-s-platform" v-model="userName" placeholder="请输入用户名" class="login-input"></el-input>
        <el-input prefix-icon="el-icon-s-order" v-model="email" placeholder="请输入邮箱" class="login-input"></el-input>
        <el-input prefix-icon="el-icon-s-ticket" v-model="tel" placeholder="请输入联系电话" class="login-input"></el-input>
        <el-button class="register-btn" type="primary" @click="saveUser" round>
          注 册
        </el-button>
        <el-button class="login-btn" plain @click="toLogin" round>
          已有账号，点此登录
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { saveUser } from '../../api/api'
export default {
  data() {
    return {
      loginAccount: "",
      password: "",
      userName: "",
      type: "1",
      email: "",
      tel: "",
    }
  },
  methods: {
    toLogin() {
      this.$router.push("/login")
    },
    saveUser() {
      if (!this.loginAccount) {
        this.$message({ message: '请输入登录账号', type: 'warning' })
        return
      }
      if (!this.password) {
        this.$message({ message: '请输入密码', type: 'warning' })
        return
      }
      if (!this.userName) {
        this.$message({ message: '请输入用户名', type: 'warning' })
        return
      }
      if (!this.email) {
        this.$message({ message: '请输入邮箱', type: 'warning' })
        return
      }
      var regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
      if (!regex.test(this.email)) {
        this.$message({ message: '请输入正确的邮箱格式', type: 'warning' })
        return
      }
      if (!this.tel) {
        this.$message({ message: '请输入联系方式', type: 'warning' })
        return
      }
      var param = {
        loginAccount: this.loginAccount,
        password: this.password,
        userName: this.userName,
        email: this.email,
        tel: this.tel,
        userType: 1,
      }
      saveUser(param).then(res => {
        if (res.code == 1000) {
          if (this.type == 2) {
            this.$message({
              message: '正在审核，审核完成后会将结果发送到您的邮箱，请注意查收',
              type: 'success'
            })
          } else {
            this.$message({
              message: '注册成功',
              type: 'success'
            })
          }
          setTimeout(() => {
            this.$router.push("/login")
          }, 500)
        } else {
          this.$message({
            message: res.message,
            type: 'warning'
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.login-bg {
  width: 100vw;
  height: 100vh;
  min-height: 500px;
  font-family: 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  background: url('../../assets/image/image2.png') no-repeat center center;
  background-size: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}
.login-bg::before {
  content: '';
  position: absolute;
  left: 0; top: 0; width: 100%; height: 100%;
  background: rgba(255,255,255,0.12);
  z-index: 0;
}
.login-center {
  width: 100vw;
  height: 100vh;
  min-height: 500px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  z-index: 1;
}
.login-box {
  width: 410px;
  min-height: 410px;
  background: rgba(255,255,255,0.96);
  box-shadow: 0 8px 32px 0 rgba(62,120,243,0.10);
  border-radius: 18px;
  padding: 38px 32px 26px 32px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.login-logo-row {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 24px;
  gap: 18px;
}
.logo-img {
  width: 80px;
  height: 80px;
}
.login-title {
  font-size: 28px;
  font-weight: bold;
  color: #d37136;
  letter-spacing: 2px;
}
.login-input {
  width: 100%;
  margin-bottom: 15px;
}
.register-btn {
  width: 100%;
  height: 44px;
  margin-bottom: 16px;
  font-size: 18px;
  font-weight: bold;
  letter-spacing: 1.5px;
  background: linear-gradient(90deg, #3E78F3 60%, #55adff 100%);
  border: none;
  color: #fff;
}
.login-btn {
  width: 100%;
  height: 44px;
  font-size: 17px;
  color: #3E78F3;
  background: #fff;
  border: 1.5px solid #3E78F3;
  font-weight: bold;
  letter-spacing: 1.5px;
  margin-bottom: 0;
  transition: background 0.2s, color 0.2s;
}
.login-btn:hover {
  background: #e4edff;
  color: #2863e6;
}
@media (max-width: 900px) {
  .login-box { width: 90vw; max-width: 400px; }
}
</style>