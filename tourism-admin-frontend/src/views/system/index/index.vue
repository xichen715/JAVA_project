<template>
  <div class="index">
    <div class="chart-card" id="index1"></div>
    <div class="chart-card" id="index2"></div>
  </div>
</template>

<script>
import { getManageData } from '../../../api/api'
import * as echarts from 'echarts'

export default {
  data() {
    return {
      myChart: null,
      myChart1: null,
      shuju: {}
    }
  },
  methods: {
    init() {
      getManageData().then(res => {
        if (res.code === 1000) {
          this.shuju = res.data

          // 景点预约图表
          const chartDom = document.getElementById('index1')
          this.myChart = echarts.init(chartDom)
          this.myChart.setOption({
            title: {
              text: '近七日景点预约',
              left: 'center',
              textStyle: { fontSize: 18, color: '#333' }
            },
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'cross',
                label: {
                  backgroundColor: '#6a7985'
                }
              }
            },
            grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
            xAxis: {
              type: 'category',
              boundaryGap: false,
              data: this.shuju.dates
            },
            yAxis: {
              type: 'value'
            },
            series: [
              {
                name: '预约数',
                type: 'line',
                smooth: true,
                showSymbol: false,
                lineStyle: { color: '#3E78F3', width: 2 },
                areaStyle: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: '#3E78F3' },
                    { offset: 1, color: 'rgba(62,120,243,0.1)' }
                  ])
                },
                emphasis: { focus: 'series' },
                data: this.shuju.nums
              }
            ]
          })

          // 酒店预约图表
          const chartDom1 = document.getElementById('index2')
          this.myChart1 = echarts.init(chartDom1)
          this.myChart1.setOption({
            title: {
              text: '近七日酒店预约',
              left: 'center',
              textStyle: { fontSize: 18, color: '#333' }
            },
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'cross',
                label: {
                  backgroundColor: '#6a7985'
                }
              }
            },
            grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
            xAxis: {
              type: 'category',
              boundaryGap: false,
              data: this.shuju.dates
            },
            yAxis: {
              type: 'value'
            },
            series: [
              {
                name: '预约数',
                type: 'line',
                smooth: true,
                showSymbol: false,
                lineStyle: { color: '#67C23A', width: 2 },
                areaStyle: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: '#67C23A' },
                    { offset: 1, color: 'rgba(103,194,58,0.1)' }
                  ])
                },
                emphasis: { focus: 'series' },
                data: this.shuju.orders
              }
            ]
          })
        }
      })
    }
  },
  mounted() {
    this.init()
    window.addEventListener('resize', () => {
      this.myChart && this.myChart.resize()
      this.myChart1 && this.myChart1.resize()
    })
  }
}
</script>

<style scoped>
.index {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 20px;
  background-color: #f4f6fa;
  height: 100%;
  box-sizing: border-box;
  overflow-y: auto;
}

.chart-card {
  background-color: #ffffff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  height: 400px;
  width: 100%;
  transition: box-shadow 0.3s ease;
}
.chart-card:hover {
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
}
</style>
