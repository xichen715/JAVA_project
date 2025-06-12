<template>
  <div class="centerPage">
    <headerPage></headerPage>
    <div class="centerPage-content">
      <el-card class="box-card">
        <div class="content">
          <div class="master">
            <el-form
                :model="user"
                :rules="rules"
                ref="ruleForm"
                label-width="110px"
                class="profile-form"
            >
              <el-form-item label="登录账号" prop="loginAccount">
                <el-input size="small" disabled v-model="user.loginAccount"></el-input>
              </el-form-item>
              <el-form-item label="用户名" prop="userName">
                <el-input size="small" v-model="user.userName"></el-input>
              </el-form-item>
              <el-form-item label="邮箱" prop="email">
                <el-input size="small" v-model="user.email"></el-input>
              </el-form-item>
              <el-form-item label="联系电话" prop="tel">
                <el-input size="small" v-model="user.tel"></el-input>
              </el-form-item>
              <el-form-item label="性别" prop="sex">
                <el-radio-group v-model="user.sex">
                  <el-radio :label="'0'">男</el-radio>
                  <el-radio :label="'1'">女</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-form>
            <div class="submit">
              <el-button type="primary" size="medium" icon="el-icon-check" @click="submit">
                保存资料
              </el-button>
            </div>
          </div>
          <div class="slave">
            <div class="img">
              <el-image
                  style="object-fit: cover;"
                  :src="$store.state.HOST + user.avatar"
                  :preview-src-list="avatar"
                  fit="cover"
                  class="avatar-img"
              ></el-image>
            </div>
            <div class="btns">
              <el-upload
                  ref="upload"
                  :action="uploadAvatarUrl() + '/' + user.id"
                  :show-file-list="false"
                  :before-upload="beforeAvatorUpload"
                  :on-success="handleAvatorSuccess"
                  accept="image/*"
              >
                <el-button class="avatar-btn" icon="el-icon-picture-outline-round">
                  更换头像
                </el-button>
              </el-upload>
              <el-button
                  class="pwd-btn"
                  icon="el-icon-key"
                  @click="changePassword"
              >修改密码</el-button>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <el-dialog
        title="修改密码"
        :visible.sync="passwordDialogVisible"
        width="340px"
        :before-close="handlePasswordClose"
        class="pwd-dialog"
    >
      <div class="pwd-dialog-content">
        <span>请输入旧密码：</span>
        <el-input
            style="margin:10px 0"
            show-password
            v-model="oldPassword"
            size="small"
            autocomplete="off"
        ></el-input>
        <span>请输入新密码：</span>
        <el-input
            style="margin:10px 0"
            show-password
            v-model="newPassword"
            size="small"
            autocomplete="off"
        ></el-input>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="handlePasswordClose">取 消</el-button>
        <el-button size="small" type="primary" @click="passwordSubmit"
        >确 定</el-button
        >
      </span>
    </el-dialog>

    <bottomPage></bottomPage>
  </div>
</template>

<script>
import { mixin } from "../../minix";
import headerPage from "../../components/header";
import bottomPage from "../../components/bottom";
import { getUser, setUserInfo, setUserAvatar, changePassword } from "../../api/api";
export default {
  mixins: [mixin],
  data() {
    var checkPhone = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("请输入联系电话"));
      } else {
        const reg = /^1[3-9]\d{9}$/;
        if (reg.test(value)) {
          callback();
        } else {
          return callback(new Error("请输入正确的联系电话"));
        }
      }
    };
    return {
      user: {},
      avatar: [],
      rules: {
        userName: [{ required: true, message: "请输入用户名", trigger: "blur" }],
        email: [
          { required: true, message: "请输入邮箱地址", trigger: "blur" },
          { type: "email", message: "请输入正确的邮箱地址", trigger: "blur" },
        ],
        tel: [
          { required: true, validator: checkPhone, message: "请输入正确的联系电话", trigger: "blur" },
        ],
      },
      oldPassword: "",
      newPassword: "",
      passwordDialogVisible: false,
    };
  },
  components: {
    headerPage,
    bottomPage,
  },
  methods: {
    handlePasswordClose() {
      this.passwordDialogVisible = false;
    },
    passwordSubmit() {
      var param = {
        id: this.user.id,
        password: this.oldPassword,
        newPassword: this.newPassword,
      };
      changePassword(param).then((res) => {
        if (res.code == 1000) {
          this.$notify.success({
            title: "成功",
            message: "密码修改成功",
          });
          this.passwordDialogVisible = false;
        } else {
          this.$notify.error({
            title: "错误",
            message: res.message,
          });
        }
      });
    },
    submit() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          setUserInfo(this.user).then((res) => {
            if (res.code == 1000) {
              this.$message({
                type: "success",
                message: "保存成功!",
              });
              this.getUserInfo();
              // 关键：同步全局用户信息
              this.$store.dispatch('updateUserInfo');
            } else {
              this.$notify.error({
                title: "错误",
                message: res.message,
              });
            }
          });
        } else {
          return false;
        }
      });
    },
    getUserInfo() {
      getUser().then((res) => {
        if (res.code == 1000) {
          this.user = res.data;
          this.user.sex = res.data.sex + "";
          this.avatar[0] = this.$store.state.HOST + this.user.avatar;
        } else {
          this.$notify.error({
            title: "错误",
            message: res.message,
          });
        }
      });
    },
    changePassword() {
      this.passwordDialogVisible = true;
    },
    handleAvatorSuccess(res) {
      if (res.code == 1000) {
        this.$message({
          type: "success",
          message: "上传成功!",
        });
        this.getUserInfo();
        // 关键：同步全局用户信息
        this.$store.dispatch('updateUserInfo');
      } else {
        this.$notify.error({
          title: "错误",
          message: res.message,
        });
      }
    },
  },
  mounted() {
    this.getUserInfo();
  },
};
</script>

<style scoped>
.centerPage {
  width: 100%;
  min-height: 100vh;
  background: #f8fafc;
}
.centerPage-content {
  width: 100%;
  display: flex;
  justify-content: center;
}
.box-card {
  margin-top: 40px;
  margin-bottom: 40px;
  width: 100%;
  max-width: 950px;
  border-radius: 18px;
  box-shadow: 0 8px 32px 0 rgba(62,120,243,0.09);
  border: none;
  background: #fff;
  transition: box-shadow 0.2s;
}
.content {
  width: 100%;
  min-height: 540px;
  display: flex;
  flex-direction: row;
  font-family: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", Arial, sans-serif;
}
.master {
  flex: 3;
  padding: 38px 32px 10px 36px;
  border-right: 1.5px solid #f0f0f0;
  background: transparent;
  min-width: 340px;
}
.profile-form ::v-deep .el-form-item__label {
  font-weight: 600;
  font-size: 15px;
  color: #3E78F3;
}
.profile-form ::v-deep .el-input__inner[disabled] {
  background: #f7fafd;
  color: #b6b6b6;
}
.profile-form ::v-deep .el-input {
  border-radius: 8px;
}
.submit {
  width: 100%;
  text-align: center;
  margin-top: 10px;
}
.submit .el-button {
  width: 160px;
  height: 40px;
  font-size: 17px;
  letter-spacing: 2px;
  border-radius: 22px;
  background: linear-gradient(90deg, #3E78F3 60%, #55adff 100%);
  color: #fff;
  border: none;
  font-weight: bold;
  transition: background 0.2s;
}
.submit .el-button:hover {
  background: linear-gradient(90deg, #2863e6 60%, #2897e6 100%);
}
.slave {
  flex: 2;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: transparent;
  padding: 0 20px;
}
.img {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: flex-end;
  margin-bottom: 18px;
}
.avatar-img {
  width: 160px !important;
  height: 160px !important;
  border-radius: 50%;
  box-shadow: 0 2px 20px 0 rgba(62,120,243,0.13);
  object-fit: cover;
  border: 4px solid #f6f8fb;
  background: #f6f8fb;
  transition: box-shadow 0.2s;
}
.btns {
  width: 100%;
  margin-top: 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.avatar-btn,
.pwd-btn {
  width: 140px;
  margin-bottom: 12px;
  border-radius: 22px;
  font-size: 16px;
  font-weight: 500;
  background: #f6f8fb;
  color: #3571d8;
  border: 1.5px solid #3E78F3;
  transition: background 0.15s, color 0.15s;
}
.avatar-btn:hover,
.pwd-btn:hover {
  background: #e4edff;
  color: #2863e6;
  border-color: #2863e6;
}
.pwd-dialog .el-dialog__header {
  text-align: center;
  font-weight: 700;
  font-size: 18px;
  color: #3E78F3;
}
.pwd-dialog-content {
  display: flex;
  flex-direction: column;
  gap: 5px;
}
@media (max-width: 900px) {
  .box-card {
    max-width: 98vw;
    margin-top: 14px;
    margin-bottom: 14px;
    padding: 0;
  }
  .content {
    flex-direction: column;
    min-height: auto;
  }
  .master, .slave {
    border: none;
    min-width: unset;
    padding: 22px 10px;
  }
  .slave {
    margin-top: 16px;
    padding: 0;
  }
  .avatar-img {
    width: 100px !important;
    height: 100px !important;
  }
  .btns .avatar-btn, .btns .pwd-btn {
    width: 80vw;
    max-width: 200px;
  }
}
</style>