<template>
  <div class="itinerary">
    <headers />
    <div class="itinerary-planner">
      <el-card>
        <el-form :inline="true" :model="input" label-width="80px">
          <el-form-item label="目的地">
            <el-input v-model="input.origin" />
          </el-form-item>
          <el-form-item label="天数">
            <el-input-number v-model="input.days" :min="1" :max="30"/>
          </el-form-item>
          <el-form-item label="偏好">
            <el-input v-model="input.preference" placeholder="美食/人文/亲子/自然等"/>
          </el-form-item>
          <el-form-item class="ai-btn-center">
            <el-button type="primary" @click="generatePlan" :loading="loading">AI生成行程</el-button>
          </el-form-item>
        </el-form>
      </el-card>
      <el-card v-if="plan.length" class="edit-card">
        <div class="plan-title">可编辑行程</div>
        <div v-for="(day, idx) in plan" :key="idx" class="plan-day">
          <el-card>
            <div class="day-header">
              <span>第{{ day.day || idx + 1 }}天</span>
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeDay(idx)" v-if="plan.length>1">删</el-button>
            </div>
            <div v-if="Array.isArray(day.activities)">
              <el-table :data="day.activities" border class="activity-table" style="width: 100%">
                <el-table-column prop="time" label="时间" width="110">
                  <template #default="scope">
                    <el-select v-model="scope.row.time" placeholder="选择时间" size="small" style="width:100px">
                      <el-option label="早上" value="早上"/>
                      <el-option label="中午" value="中午"/>
                      <el-option label="晚上" value="晚上"/>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column prop="location" label="景点">
                  <template #default="scope">
                    <el-input v-model="scope.row.location" size="small" placeholder="填写景点" style="width:180px"/>
                  </template>
                </el-table-column>
                <el-table-column prop="food" label="美食">
                  <template #default="scope">
                    <el-input v-model="scope.row.food" size="small" placeholder="填写美食" style="width:180px"/>
                  </template>
                </el-table-column>
                <el-table-column prop="experience" label="体验">
                  <template #default="scope">
                    <el-input
                        v-model="scope.row.experience"
                        type="textarea"
                        :rows="3"
                        size="small"
                        placeholder="填写体验"
                        class="experience-textarea"
                    />
                  </template>
                </el-table-column>
                <el-table-column width="60">
                  <template #header>
                    <el-button type="success" icon="el-icon-plus" @click="addActivity(day)" size="mini" circle />
                  </template>
                  <template #default="scope">
                    <el-button type="danger" icon="el-icon-minus" @click="removeActivity(day, scope.$index)" size="mini" circle v-if="day.activities.length>1"/>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <div v-else>
              <el-input type="textarea" :rows="4" v-model="day.content" placeholder="请编辑当天行程内容"/>
            </div>
          </el-card>
        </div>
        <div class="plan-btns">
          <el-button type="success" icon="el-icon-plus" size="small" @click="addDay">添加一天</el-button>
          <el-button type="primary" icon="el-icon-check" style="margin-left:20px" @click="savePlan">保存行程</el-button>
        </div>
      </el-card>
      <el-dialog title="保存成功" :visible.sync="dialogVisible">
        <span>您的行程已保存，刷新页面依然可用！</span>
        <span slot="footer">
          <el-button type="primary" @click="dialogVisible = false">确定</el-button>
        </span>
      </el-dialog>
    </div>
    <bottom-page />
  </div>
</template>

<script>
import axios from "axios";
import headers from "@/components/header.vue";
import BottomPage from "@/components/bottom.vue";
export default {
  components: {BottomPage, headers},
  data() {
    return {
      input: {
        origin: "",
        days: 5,
        preference: ""
      },
      plan: [],
      loading: false,
      dialogVisible: false,
    }
  },
  methods: {
    async generatePlan() {
      if (!this.input.origin || !this.input.days) {
        this.$message.error("请填写出发地和天数");
        return;
      }
      this.loading = true;
      try {
        // 优化prompt，要求AI返回对象数组
        const prompt = `请帮我规划${this.input.origin}出发${this.input.days}天${this.input.preference || '美食'}旅游行程。要求如下：
1. 每天分为早上、中午、晚上三段，每段都要包含具体景点、美食和体验内容，内容要详细丰富。
2. 只返回一个JSON数组，数组中每一项是一个对象，对象结构如下：{"day": <天数>, "activities": [{"time": "早上/中午/晚上", "location": "地点", "food": "美食", "experience": "体验"}]}
3. 不要输出任何除纯数组以外的内容，不要输出注释、说明、代码块等。
4. 每天的内容建议不少于80字。

示例输出（仅供格式参考）：
[
  {
    "day": 1,
    "activities": [
      {"time": "早上", "location": "王府井大街", "food": "炒肝、炒肉串", "experience": "体验地道北京早点"},
      {"time": "中午", "location": "老北京炸酱面", "food": "炸酱面", "experience": "品尝正宗炸酱面"},
      {"time": "晚上", "location": "全聚德", "food": "烤鸭", "experience": "享用正宗北京烤鸭"}
    ]
  }
]`;
        const apiKey = "sk-nVvBA5woPjn6nFjcvkNfIf647QVJKmZ32hpqcarNbkmlg0Hh"; // 填写你的plus7.plus平台API Key
        const res = await axios.post(
            "https://lonlie.plus7.plus/v1/chat/completions",
            {
              model: "gpt-3.5-turbo",
              messages: [{ role: "user", content: prompt }],
              max_tokens: 2000,
            },
            {
              headers: { Authorization: "Bearer " + apiKey }
            }
        );
        let txt = res.data.choices[0].message.content.trim();

        // 容错：去除常见代码块包裹
        if (txt.startsWith("```json")) txt = txt.replace(/```json|```/g, "");
        if (txt.startsWith("```")) txt = txt.replace(/```/g, "");

        // 容错：AI返回对象/数组都兼容
        try {
          let result = JSON.parse(txt);
          if (Array.isArray(result)) {
            this.plan = result;
          } else if (typeof result === 'object' && result !== null) {
            this.plan = [result];
          } else {
            this.plan = [{ content: "AI原文如下：\n" + txt }];
          }
        } catch (err) {
          this.plan = [{ content: "AI原文如下：\n" + txt }];
        }
      } catch (e) {
        this.$message.error("AI生成失败，请检查API Key或网络");
      } finally {
        this.loading = false;
      }
    },
    addDay() {
      this.plan.push({
        day: this.plan.length + 1,
        activities: [
          { time: "早上", location: "", food: "", experience: "" },
          { time: "中午", location: "", food: "", experience: "" },
          { time: "晚上", location: "", food: "", experience: "" }
        ]
      });
    },
    removeDay(idx) { this.plan.splice(idx, 1); },
    addActivity(day) {
      day.activities.push({ time: "早上", location: "", food: "", experience: "" });
    },
    removeActivity(day, idx) {
      if (day.activities.length > 1) day.activities.splice(idx, 1);
    },
    savePlan() {
      localStorage.setItem("userItinerary", JSON.stringify(this.plan));
      this.dialogVisible = true;
    }
  },
  mounted() {
    const local = localStorage.getItem("userItinerary");
    if (local) this.plan = JSON.parse(local);
  }
}
</script>

<style scoped>
.itinerary-planner {
  max-width: 900px;
  margin: 32px auto;
  background: #f7f9fa;
  padding-bottom: 32px;
  border-radius: 18px;
}

.edit-card {
  margin-top: 24px;
  background: #fcfcfe;
  box-shadow: 0 4px 24px rgba(50,100,255,0.08);
  border-radius: 18px;
  border: none;
}

.plan-title {
  font-size: 23px;
  margin: 0 0 16px 0;
  font-weight: 600;
  color: #3253a8;
  padding-left: 12px;
}

.plan-day {
  margin-bottom: 18px;
  border-radius: 14px;
  box-shadow: 0 2px 12px rgba(67,129,255,0.06);
  background: #fff;
}

.day-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 17px;
  font-weight: 500;
  color: #2f436e;
  padding: 8px 0 0 4px;
}

.activity-table .el-input,
.activity-table .el-select {
  min-width: 60px;
}

.activity-table .el-table__body td:nth-child(2) .el-input {  /* 景点 */
  width: 180px;
}
.activity-table .el-table__body td:nth-child(3) .el-input {  /* 美食 */
  width: 180px;
}
.activity-table .experience-textarea .el-textarea__inner, /* 体验 - 多行文本 */
.activity-table .el-table__body td:nth-child(4) .el-textarea__inner {
  width: 320px;
  min-height: 60px;
  resize: vertical;
  font-size: 15px;
  line-height: 1.6;
}

.el-table th, .el-table td {
  background: #f7fbff;
}

.el-table th {
  color: #3253a8;
  font-weight: 600;
  font-size: 15px;
}

.el-button--danger {
  background: #ffebee !important;
  color: #e53935 !important;
  border: none;
}
.el-button--danger:hover {
  background: #ffcdd2 !important;
}
.el-button--success {
  background: #e8f5e9 !important;
  color: #43a047 !important;
  border: none;
}
.el-button--success:hover {
  background: #b2dfdb !important;
}
.el-button--primary {
  background: #e3e8f7 !important;
  color: #3253a8 !important;
  border: none;
}
.el-button--primary:hover {
  background: #bfd2f5 !important;
}

/* 新增按钮居中样式 */
.plan-btns {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 16px 0 0 0;
}

/* AI生成按钮居中 */
.el-form .ai-btn-center {
  display: flex;
  justify-content: center;
  width: 100%;
}

@media (max-width: 800px) {
  .activity-table .el-table__body td:nth-child(2) .el-input,
  .activity-table .el-table__body td:nth-child(3) .el-input,
  .activity-table .experience-textarea .el-textarea__inner,
  .activity-table .el-table__body td:nth-child(4) .el-textarea__inner {
    width: 100% !important;
    min-width: 60px;
  }
}
@media (max-width: 600px) {
  .itinerary-planner { max-width: 100%; padding: 4px; }
  .edit-card { margin-top: 6px; border-radius: 12px; }
  .plan-title { font-size: 17px; }
  .plan-day { margin-bottom: 8px; border-radius: 10px;}
  .day-header { font-size: 14px; }
}
</style>