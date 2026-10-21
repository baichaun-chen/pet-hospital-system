<template>
  <!-- 首页容器 -->
    <div class="dashboard">
        <!-- 统计卡片 -->
        <el-row :gutter="20" class="mb20">
            <el-col :span="6">
                <el-card class="stat-card" shadow="hover">
                    <div class="stat-card-content">
                        <div class="stat-card-icon" style="background-color: #40c9c6">
                            <User style="color: #fff"/>
                        </div>
                        <div class="stat-card-text">
                            <div class="stat-card-title">总用户数</div>
                            <div class="stat-card-number">{{ countData.userCount }}</div>
                        </div>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card class="stat-card" shadow="hover">
                    <div class="stat-card-content">
                        <div class="stat-card-icon" style="background-color: #36a3f7">
                            <Calendar style="color: #fff"/>
                        </div>
                        <div class="stat-card-text">
                            <div class="stat-card-title">预约数</div>
                            <div class="stat-card-number">{{ countData.reservationCount }}</div>
                        </div>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card class="stat-card" shadow="hover">
                    <div class="stat-card-content">
                        <div class="stat-card-icon" style="background-color: #f4516c">
                            <Avatar style="color: #fff"/>
                        </div>
                        <div class="stat-card-text">
                            <div class="stat-card-title">医生数</div>
                            <div class="stat-card-number">{{ countData.doctorCount }}</div>
                        </div>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card class="stat-card" shadow="hover">
                    <div class="stat-card-content">
                        <div class="stat-card-icon" style="background-color: #34bfa3">
                            <VideoCamera style="color: #fff"/>
                        </div>
                        <div class="stat-card-text">
                            <div class="stat-card-title">疫苗数</div>
                            <div class="stat-card-number">{{ countData.vaccineCount }}</div>
                        </div>
                    </div>
                </el-card>
            </el-col>
        </el-row>

        <!-- 图表区域 -->
        <el-row :gutter="20" class="mb20">
            <!-- 左侧: 数据统计折现图 -->
            <el-col :span="16">
                <el-card shadow="hover">
                    <!-- 卡片头部插槽 -->
                    <template #header>
                        <div class="card-header">
                            <span>数据统计</span>
                        </div>
                    </template>
                    <!-- 图表容器 -->
                    <div ref="chartRef" style="height: 500px"/>
                </el-card>
            </el-col>

            <!-- 右侧: 预约状态饼图 -->
            <el-col :span="8">
                <el-card shadow="hover">
                    <!-- 卡片头部插槽 -->
                    <template #header>
                        <div class="card-header">
                            <span>预约状态</span>
                        </div>
                    </template>
                    <!-- 图表容器 -->
                    <div ref="pieChartRef" style="height: 500px"/>
                </el-card>
            </el-col>
        </el-row>

    </div>
</template>

<script setup>
import {onMounted, ref} from "vue";
import {selectHomeCount, selectReservationStat, selectReservationStatusChart} from "@/api/hospital/homePage.js";
import {Avatar, Calendar, User, VideoCamera} from "@element-plus/icons-vue";
import * as echarts from 'echarts'

//统计数据
const countData = ref({})

//近7天预约统计数据
const reservationStat = ref({})

//预约状态分布数据
const reservationStatusStat = ref({})

const chartRef = ref()          // 折线图容器引用

const pieChartRef = ref() //饼图容器引用

//组件挂载完成后执行
onMounted(() => {
    selectHomeCount().then(res => {
        countData.value = res.data
    })

    //获取预约统计数据
    selectReservationStat().then(res => {
        reservationStat.value = res.data
        selectReservationStatusChart().then(res => {
            reservationStatusStat.value = res.data
            //初始化图表
            initChart()
        })
    })
})

let chartInstance = null
let pieChartInstance = null

//初始化图表函数
const initChart = () => {
    // 初始化折线图
    chartInstance = echarts.init(chartRef.value)

    // 折线图配置选项
    const option = {
        title: {
            text: '近7天预约统计'  // 图表标题
        },
        tooltip: {
            trigger: 'axis'        // 触发类型：坐标轴触发
        },
        xAxis: {
            type: 'category',      // 类目轴
            data: reservationStat.value.date  // X轴数据（日期）
        },
        yAxis: {
            type: 'value'          // 数值轴
        },
        series: [
            {
                data: reservationStat.value.count,  // 系列数据（数量）
                type: 'line',       // 折线图
                smooth: true,       // 平滑曲线
                areaStyle: {}       // 区域填充样式
            }
        ]
    }
    // 设置折线图选项
    chartInstance.setOption(option)

    // xxx统计图
    pieChartInstance = echarts.init(pieChartRef.value)
    const pieOption = {
        tooltip: {
            trigger: 'item'
        },
        legend: {
            bottom: 'bottom'
        },
        series: [
            {
                name: '预约状态',
                type: 'pie',
                radius: ['40%', '70%'],
                avoidLabelOverlap: false,
                itemStyle: {
                    borderRadius: 10,
                    borderColor: '#fff',
                    borderWidth: 2
                },
                label: {
                    show: false,
                    position: 'center'
                },
                emphasis: {
                    label: {
                        show: true,
                        fontSize: '18',
                        fontWeight: 'bold'
                    }
                },
                labelLine: {
                    show: false
                },
                data: reservationStatusStat.value
            }
        ]
    }
    pieChartInstance.setOption(pieOption)
}

</script>

<style scoped>
/* 仪表盘主容器样式 */
.dashboard {
    padding: 20px; /* 内边距 */
    min-height: calc(100vh - 84px); /* 最小高度：视口高度减去84px */
}

/* 底部外边距样式 */
.mb20 {
    margin-bottom: 50px; /* 底部外边距50px */
}

/* 统计卡片样式 */
.stat-card {
    border-radius: 8px; /* 圆角边框 */
}

/* 统计卡片内容区域样式 */
.stat-card-content {
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中对齐 */
}

/* 统计卡片图标容器样式 */
.stat-card-icon {
    width: 60px; /* 固定宽度 */
    height: 60px; /* 固定高度 */
    border-radius: 8px; /* 圆角边框 */
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中对齐 */
    justify-content: center; /* 水平居中对齐 */
    margin-right: 15px; /* 右侧外边距 */
}

/* 统计卡片文本区域样式 */
.stat-card-text {
    flex: 1; /* 占据剩余空间 */
}

/* 统计卡片标题样式 */
.stat-card-title {
    font-size: 14px; /* 字体大小 */
    color: #999; /* 字体颜色（灰色） */
    margin-bottom: 5px; /* 底部外边距 */
}

/* 统计卡片数字样式 */
.stat-card-number {
    font-size: 24px; /* 字体大小 */
    font-weight: bold; /* 字体粗细：粗体 */
    color: #333; /* 字体颜色（深灰色） */
}

/* 卡片头部样式 */
.card-header {
    display: flex; /* 弹性布局 */
    justify-content: space-between; /* 两端对齐 */
    align-items: center; /* 垂直居中对齐 */
}
</style>
