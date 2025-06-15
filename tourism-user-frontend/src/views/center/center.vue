<template>
  <div class="centerPage">
    <headerPage />
    <div class="centerPage-content">
      <el-card class="box-card">
        <el-tabs v-model="activeTab" type="card" class="center-tabs">
          <el-tab-pane label="资料管理" name="profile">
            <div class="content">
              <div class="master">
                <!-- 资料表单同原样 -->
                <el-form
                    :model="user"
                    :rules="rules"
                    ref="ruleForm"
                    label-width="110px"
                    class="profile-form"
                >
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
          </el-tab-pane>
          <el-tab-pane label="我的收藏" name="favor">
            <div class="favor-list">
              <div
                  class="favor-item"
                  v-for="item in favorList"
                  :key="item.lineId"
                  @click="toLineInfo(item.lineId)"
              >
                <img class="favor-img" :src="item.images?.split(',')[0]" :alt="item.name" />
                <div class="favor-info">
                  <div class="favor-title">{{ item.name }}</div>
                  <div class="favor-desc">{{ item.introduce }}</div>
                </div>
              </div>
              <div v-if="!favorList.length" class="favor-empty">暂无收藏线路</div>
              <el-pagination
                  background
                  :page-size="favorSearch.pageSize"
                  layout="prev, pager, next"
                  @current-change="handleFavorPageChange"
                  :total="favorTotal"
                  class="favor-pagination"
              />
            </div>
          </el-tab-pane>
          <el-tab-pane label="我的预定" name="order">
            <div class="order-list">
              <el-tabs v-model="orderTab" type="border-card" stretch>
                <el-tab-pane label="景点" name="attraction">
                  <div class="order-cards">
                    <div
                        class="order-item"
                        v-for="item in attractionOrders"
                        :key="item.id"
                        @click="showAttractionDialog(item)"
                    >
                      <img class="order-img" :src="item.images?.split(',')[0]" :alt="item.name" />
                      <div class="order-info">
                        <div class="order-title">{{ item.name }}</div>
                        <div class="order-desc">{{ item.introduce }}</div>
                        <div class="order-state">
                          <el-tag v-if="item.state == 0">未确认</el-tag>
                          <el-tag type="success" v-else-if="item.state == 1">已确认</el-tag>
                          <el-tag type="info" v-else-if="item.state == 2">取消中</el-tag>
                          <el-tag type="info" v-else-if="item.state == 3">已取消</el-tag>
                          <el-tag type="danger" v-else-if="item.state == 4">取消失败</el-tag>
                          <el-tag type="warning" v-else-if="item.state == 5">已使用</el-tag>
                        </div>
                      </div>
                    </div>
                    <div v-if="!attractionOrders.length" class="order-empty">暂无景点预定</div>
                    <el-pagination
                        background
                        :page-size="orderSearch.pageSize"
                        layout="prev, pager, next"
                        @current-change="handleOrderPageChange"
                        :total="orderTotal"
                        class="order-pagination"
                    />
                  </div>
                </el-tab-pane>
                <el-tab-pane label="酒店" name="hotel">
                  <div class="order-cards">
                    <div
                        class="order-item"
                        v-for="item in hotelOrders"
                        :key="item.id"
                        @click="showHotelDialog(item)"
                    >
                      <img class="order-img" :src="item.images?.split(',')[0]" :alt="item.name" />
                      <div class="order-info">
                        <div class="order-title">{{ item.name }}</div>
                        <div class="order-desc">{{ item.introduce }}</div>
                        <div class="order-state">
                          <el-tag v-if="item.state == 0">未确认</el-tag>
                          <el-tag type="success" v-else-if="item.state == 1">已确认</el-tag>
                          <el-tag type="info" v-else-if="item.state == 2">取消中</el-tag>
                          <el-tag type="info" v-else-if="item.state == 3">已取消</el-tag>
                          <el-tag type="danger" v-else-if="item.state == 4">取消失败</el-tag>
                          <el-tag type="warning" v-else-if="item.state == 5">已使用</el-tag>
                        </div>
                      </div>
                    </div>
                    <div v-if="!hotelOrders.length" class="order-empty">暂无酒店预定</div>
                    <el-pagination
                        background
                        :page-size="hotelOrderSearch.pageSize"
                        layout="prev, pager, next"
                        @current-change="handleHotelOrderPageChange"
                        :total="hotelOrderTotal"
                        class="order-pagination"
                    />
                  </div>
                </el-tab-pane>
              </el-tabs>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </div>
    <el-dialog
        title="修改密码"
        :visible.sync="passwordDialogVisible"
        width="420px"
    >
      <el-form ref="passwordForm" label-width="120px">
        <el-form-item label="旧密码">
          <el-input v-model="oldPassword" type="password" placeholder="请输入旧密码" />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="newPassword" type="password" placeholder="请输入新密码" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="handlePasswordClose">取消</el-button>
    <el-button type="primary" @click="passwordSubmit">确认修改</el-button>
  </span>
    </el-dialog>
    <el-dialog
        title="景点预约详情"
        :visible.sync="attractionDialogVisible"
        width="420px"
    >
      <div v-if="selectedAttraction">
        <div style="margin-bottom:10px">
          <el-tag v-if="selectedAttraction.state == 0">未确认</el-tag>
          <el-tag type="success" v-else-if="selectedAttraction.state == 1">已确认</el-tag>
          <el-tag type="info" v-else-if="selectedAttraction.state == 2">取消中</el-tag>
          <el-tag type="info" v-else-if="selectedAttraction.state == 3">已取消</el-tag>
          <el-tag type="danger" v-else-if="selectedAttraction.state == 4">取消失败</el-tag>
          <el-tag type="warning" v-else-if="selectedAttraction.state == 5">已使用</el-tag>
        </div>
        预约人数：{{ selectedAttraction.num }}<br>
        预约日期：{{ selectedAttraction.time }}<br>
        预约人信息：
        <div v-for="(p, idx) in JSON.parse(selectedAttraction.people)" :key="idx" style="margin-bottom:6px">
          姓名：{{ p.name }}，电话：{{ p.tel }}，身份证：{{ p.idCard }}
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="attractionDialogVisible = false" size="small">关 闭</el-button>
        <el-button type="primary" size="small"
                   v-if="selectedAttraction && ![2,3,4,5].includes(selectedAttraction.state)"
                   @click="cancelAttractionOrder(selectedAttraction)"
        >取消预约</el-button>
      </span>
    </el-dialog>
    <el-dialog
        title="酒店预约详情"
        :visible.sync="hotelDialogVisible"
        width="420px"
    >
      <div v-if="selectedHotel">
        <div style="margin-bottom:10px">
          <el-tag v-if="selectedHotel.state == 0">未确认</el-tag>
          <el-tag type="success" v-else-if="selectedHotel.state == 1">已确认</el-tag>
          <el-tag type="info" v-else-if="selectedHotel.state == 2">取消中</el-tag>
          <el-tag type="info" v-else-if="selectedHotel.state == 3">已取消</el-tag>
          <el-tag type="danger" v-else-if="selectedHotel.state == 4">取消失败</el-tag>
          <el-tag type="warning" v-else-if="selectedHotel.state == 5">已使用</el-tag>
        </div>
        预约人数：{{ selectedHotel.num }}<br>
        预约日期：{{ selectedHotel.time }}<br>
        预约人信息：
        <div v-if="selectedHotel.people">
          <div>
            姓名：{{ JSON.parse(selectedHotel.people).name }}，电话：{{ JSON.parse(selectedHotel.people).tel }}，身份证：{{ JSON.parse(selectedHotel.people).idCard }}
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="hotelDialogVisible = false" size="small">关 闭</el-button>
        <el-button type="primary" size="small"
                   v-if="selectedHotel && ![2,3,4,5].includes(selectedHotel.state)"
                   @click="cancelHotelOrder(selectedHotel)"
        >取消预约</el-button>
      </span>
    </el-dialog>
    <bottomPage />
  </div>
</template>

<script>
import { mixin } from "../../minix";
import headerPage from "../../components/header";
import bottomPage from "../../components/bottom";
import {
  getUser,
  setUserInfo,
  setUserAvatar,
  changePassword,
  getSysFavorPage,
  getSysAttractionOrderPage,
  getSysHotelOrderPage,
  editSysAttractionOrder,
  editSysHotelOrder
} from "../../api/api";

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
      // Tab控制
      activeTab: "profile",
      // 资料相关
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

      // 收藏相关
      favorSearch: {
        userId: "",
        pageSize: 6,
        pageNumber: 1,
      },
      favorTotal: 0,
      favorList: [],

      // 预定相关
      orderTab: "attraction",
      orderSearch: {
        userId: "",
        pageSize: 6,
        pageNumber: 1,
      },
      orderTotal: 0,
      attractionOrders: [],
      hotelOrderSearch: {
        userId: "",
        pageSize: 6,
        pageNumber: 1,
      },
      hotelOrderTotal: 0,
      hotelOrders: [],

      // 预约详情弹窗
      attractionDialogVisible: false,
      selectedAttraction: null,
      hotelDialogVisible: false,
      selectedHotel: null,
    };
  },
  components: {
    headerPage,
    bottomPage,
  },
  methods: {
    // ========== 个人资料 ==========
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
          // 自动加载收藏和预定数据
          this.favorSearch.userId = this.user.id;
          this.orderSearch.userId = this.user.id;
          this.hotelOrderSearch.userId = this.user.id;
          this.getFavorList();
          this.getAttractionOrders();
          this.getHotelOrders();
        } else {
          this.$notify.error({
            title: "错误",
            message: res.message,
          });
        }
      });
    },
    changePassword() {
      console.log("修改密码弹窗触发");
      this.passwordDialogVisible = true;
    },
    handleAvatorSuccess(res) {
      if (res.code == 1000) {
        this.$message({
          type: "success",
          message: "上传成功!",
        });
        this.getUserInfo();
        this.$store.dispatch('updateUserInfo');
      } else {
        this.$notify.error({
          title: "错误",
          message: res.message,
        });
      }
    },
    toLineInfo(id) {
      this.$router.push("/lineInfo?id=" + id)
    },
    // ========== 收藏 ==========
    getFavorList() {
      getSysFavorPage(this.favorSearch).then(res => {
        if (res.code == 1000) {
          this.favorList = res.data.records
          this.favorTotal = res.data.total
        } else {
          this.favorList = []
          this.favorTotal = 0
        }
      });
    },
    handleFavorPageChange(page) {
      this.favorSearch.pageNumber = page;
      this.getFavorList();
    },
    // ========== 预定 ==========
    getAttractionOrders() {
      getSysAttractionOrderPage(this.orderSearch).then(res => {
        if (res.code == 1000) {
          this.attractionOrders = res.data.records;
          this.orderTotal = res.data.total;
        } else {
          this.attractionOrders = [];
          this.orderTotal = 0;
        }
      });
    },
    handleOrderPageChange(page) {
      this.orderSearch.pageNumber = page;
      this.getAttractionOrders();
    },
    getHotelOrders() {
      getSysHotelOrderPage(this.hotelOrderSearch).then(res => {
        if (res.code == 1000) {
          this.hotelOrders = res.data.records;
          this.hotelOrderTotal = res.data.total;
        } else {
          this.hotelOrders = [];
          this.hotelOrderTotal = 0;
        }
      });
    },
    handleHotelOrderPageChange(page) {
      this.hotelOrderSearch.pageNumber = page;
      this.getHotelOrders();
    },
    // ========== 预约详情弹窗 ==========
    showAttractionDialog(item) {
      this.selectedAttraction = item;
      this.attractionDialogVisible = true;
    },
    showHotelDialog(item) {
      this.selectedHotel = item;
      this.hotelDialogVisible = true;
    },
    cancelAttractionOrder(item) {
      editSysAttractionOrder({id: item.id, state: 2}).then(res => {
        if(res.code == 1000) {
          this.$message.success('取消申请成功,请等待审核通过');
          this.attractionDialogVisible = false;
          this.getAttractionOrders();
        }
      });
    },
    cancelHotelOrder(item) {
      editSysHotelOrder({id: item.id, state: 2}).then(res => {
        if(res.code == 1000) {
          this.$message.success('取消申请成功,请等待审核通过');
          this.hotelDialogVisible = false;
          this.getHotelOrders();
        }
      });
    }
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
  max-width: 1200px;
  border-radius: 18px;
  box-shadow: 0 8px 32px 0 rgba(62,120,243,0.09);
  border: none;
  background: #fff;
  transition: box-shadow 0.2s;
}
.center-tabs {
  width: 100%;
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
/* ========== 我的收藏 ========== */
.favor-list {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: 340px;
}
.favor-item {
  width: 96%;
  margin: 24px auto 0 auto;
  display: flex;
  align-items: center;
  background: #fafbfc;
  border-radius: 14px;
  box-shadow: 0 2px 8px #e9efff;
  cursor: pointer;
  transition: box-shadow 0.18s;
  min-height: 120px;
  padding: 18px 24px;
}
.favor-item:hover {
  box-shadow: 0 4px 24px #bfd5f5;
}
.favor-img {
  width: 140px;
  height: 90px;
  border-radius: 10px;
  object-fit: cover;
  background: #eee;
  margin-right: 22px;
}
.favor-info {
  flex: 1;
  min-width: 0;
}
.favor-title {
  font-size: 18px;
  font-weight: bold;
  color: #244;
  margin-bottom: 6px;
  line-height: 1.2;
  word-break: break-all;
}
.favor-desc {
  color: #444;
  font-size: 14px;
  line-height: 1.7;
  max-height: 46px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.favor-empty {
  color: #aaa;
  font-size: 18px;
  padding: 48px 0;
  text-align: center;
}
.favor-pagination {
  margin: 18px 0 8px 0;
  align-self: center;
}
/* ========== 我的预定 ========== */
.order-list {
  width: 100%;
  min-height: 340px;
}
.order-cards {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.order-item {
  width: 96%;
  margin: 24px auto 0 auto;
  display: flex;
  align-items: center;
  background: #f8faff;
  border-radius: 14px;
  box-shadow: 0 2px 8px #e9efff;
  cursor: pointer;
  transition: box-shadow 0.18s;
  min-height: 120px;
  padding: 18px 24px;
}
.order-item:hover {
  box-shadow: 0 4px 24px #bfd5f5;
}
.order-img {
  width: 140px;
  height: 90px;
  border-radius: 10px;
  object-fit: cover;
  background: #eee;
  margin-right: 22px;
}
.order-info {
  flex: 1;
  min-width: 0;
}
.order-title {
  font-size: 18px;
  font-weight: bold;
  color: #244;
  margin-bottom: 6px;
  line-height: 1.2;
  word-break: break-all;
}
.order-desc {
  color: #444;
  font-size: 14px;
  line-height: 1.7;
  max-height: 46px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.order-state {
  margin-top: 6px;
}
.order-empty {
  color: #aaa;
  font-size: 18px;
  padding: 48px 0;
  text-align: center;
}
.order-pagination {
  margin: 18px 0 8px 0;
  align-self: center;
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
  .favor-item, .order-item {
    flex-direction: column;
    padding: 10px 6px;
    min-height: 80px;
    width: 99vw;
  }
  .favor-img, .order-img {
    width: 99vw;
    max-width: 220px;
    min-width: 80px;
    height: 70px;
    margin-bottom: 10px;
    margin-right: 0;
  }
}
</style>