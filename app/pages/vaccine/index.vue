<template>
  <!-- 页面容器 -->
    <view class="vaccination-container">
        <!-- 疫苗接种说明区域 -->
        <view class="info-section">
            <view class="section-header">
                <text class="section-title">疫苗接种须知</text>
            </view>
            <view class="info-content">
                <!-- 说明文本 -->
                <text class="info-text">
                    疫苗接种是预防宠物疾病的重要手段。根据宠物种类、年龄和健康状况，
                    我们为您提供了科学的疫苗接种计划。请在接种前仔细阅读以下注意事项：
                </text>

                <!-- 注意事项列表 -->
                <view class="bullet-points">
                    <view class="point-item">
                        <text class="bullet">•</text>
                        <text class="point-text">接种前确保宠物身体健康，无发热、腹泻等症状</text>
                    </view>
                    <view class="point-item">
                        <text class="bullet">•</text>
                        <text class="point-text">幼犬/幼猫首次接种疫苗通常在6-8周龄开始</text>
                    </view>
                    <view class="point-item">
                        <text class="bullet">•</text>
                        <text class="point-text">接种后需观察15-30分钟，以防过敏反应</text>
                    </view>
                    <view class="point-item">
                        <text class="bullet">•</text>
                        <text class="point-text">接种后24小时内避免洗澡和剧烈运动</text>
                    </view>
                    <view class="point-item">
                        <text class="bullet">•</text>
                        <text class="point-text">严格按照免疫程序完成所有剂次接种</text>
                    </view>
                </view>
            </view>
        </view>

        <!-- 疫苗选择区域 -->
        <view class="form-section">
            <view class="section-header">
                <text class="section-title">选择疫苗</text>
            </view>
            <!-- 疫苗列表 -->
            <view class="vaccine-list">
                <view class="vaccine-item"
                      v-for="vaccine in vaccineList"
                      :key="vaccine.vaccineId"
                      :class="{ active: selectedVaccineId === vaccine.vaccineId }"
                      @click="selectVaccine(vaccine.vaccineId)"
                >
                    <view class="vaccine-info">
                        <text class="vaccine-name">{{ vaccine.name }}</text>
                        <text class="vaccine-desc">{{ vaccine.description }}</text>
                    </view>
                    <!--选中状态标识-->
                    <view class="vaccine-status" v-if="selectedVaccineId === vaccine.vaccineId">
                        <text class="iconfont icon-code"/>
                    </view>
                </view>
            </view>
        </view>

        <!--宠物信息填写区域-->
        <view class="form-section">
            <view class="section-header">
                <text class="section-title">宠物信息</text>
            </view>
            <!--表单-->
            <view class="pet-form">
                <view class="form-item">
                    <text class="label">宠物名字</text>
                    <input v-model="petInfo.petName" placeholder="请输入宠物名字" class="input"/>
                </view>
                <view class="form-item">
                    <text class="label">宠物品种</text>
                    <input v-model="petInfo.petBread" placeholder="请输入宠物品种" class="input"/>
                </view>
                <view class="form-item">
                    <text class="label">宠物年龄</text>
                    <input v-model="petInfo.petAge" placeholder="请输入宠物年龄" class="input"/>
                </view>
            </view>
        </view>

        <!--时间选择区域-->
        <view class="form-section">
            <view class="section-header">
                <text class="section-title">选择时间</text>
            </view>
            <view class="datetime-picker">
                <!--日期-->
                <view class="picker-item">
                    <text class="label">预约日期</text>
                    <picker mode="date" :value="date" @change="dateChange">
                        <view class="picker-display">
                            <text v-if="date" class="selected-value">{{ date }}</text>
                            <text v-else class="placeholder">请选择日期</text>
                        </view>
                    </picker>
                </view>

                <!--时间-->
                <view class="picker-item">
                    <text class="label">预约时间</text>
                    <picker mode="time" :value="time" @change="timeChange">
                        <view class="picker-display">
                            <text v-if="time" class="selected-value">{{ time }}</text>
                            <text v-else class="placeholder">请选择时间</text>
                        </view>
                    </picker>
                </view>
            </view>
        </view>

        <!--底部提交按钮-->
        <view class="action-bar">
            <!--提交按钮-->
            <view class="submit-btn" @click="submit">
                <text>提交预约</text>
            </view>
        </view>

    </view>
</template>

<script setup>
import {ref} from 'vue'
import {onShow} from "@dcloudio/uni-app";
import {listVaccine} from "@/pages/api/hospital/vaccine";
import {addReservation} from "@/pages/api/hospital/reservation";

//选择的疫苗ID
const selectedVaccineId = ref(null)

//选择的日期
const date = ref('')
//选择的时间
const time = ref('')

//宠物信息表单
const petInfo = ref({
    petName: null, //宠物名字
    petBread: null, //宠物品种
    petAge: null, //宠物年龄
})

//查询参数
const query = ref({
    pageNum: 1,
    pageSize: 100,
})

//疫苗列表数据
const vaccineList = ref([])

//选择疫苗
const selectVaccine = (vaccineId) => {
    selectedVaccineId.value = vaccineId
}

//选择日期
const dateChange = (e) => {
    date.value = e.detail.value
}

//选择时间
const timeChange = (e) => {
    time.value = e.detail.value
}

//提交预约
const submit = () => {
    //验证宠物信息是否填写完整
    if (!petInfo.value.petName || !petInfo.value.petBread || !petInfo.value.petAge) {
        uni.showToast({
            title: '请填写宠物信息',
            icon: "none"
        })
        return
    }

    //验证是否选择了疫苗 日期 时间
    if (!selectedVaccineId.value || !date.value || !time.value) {
        uni.showToast({
            title: '请完善预约信息',
            icon: "none"
        })
        return
    }

    //显示加载状态
    uni.showLoading({
        title: '提交中...'
    })

    const form = {
        type: '疫苗预约',
        petName: petInfo.value.petName,
        petBread: petInfo.value.petBread,
        petAge: petInfo.value.petAge,
        symptom: '疫苗接种预约',
        date: date.value,
        time: time.value,
        vaccineId: selectedVaccineId.value,
    }

    //调用API提交预约
    addReservation(form).then(res => {
        //关闭加载状态
        uni.hideLoading()
        //显示成功提示
        uni.showToast({
            title: '预约成功',
            icon: "success"
        })

        //跳转到预约记录页面
        uni.switchTab({
            url: '/pages/records/index'
        })
    })

}

//页面显示时加载
onShow(() => {
    listVaccine(query.value).then(res => {
        vaccineList.value = res.rows
    })
})

</script>

<style scoped>
/* 页面容器样式 */
.vaccination-container {
    background-color: #f8f9fa; /* 浅灰色背景 */
    min-height: 100vh; /* 最小高度为视口高度 */
    padding-bottom: 150rpx; /* 底部内边距，为操作栏留出空间 */
}

/* 信息说明区域样式 */
.info-section {
    background-color: #fff; /* 白色背景 */
    margin-bottom: 20rpx; /* 底部外边距 */
    padding: 0 30rpx; /* 左右内边距 */
}

/* 区域头部样式 */
.section-header {
    display: flex; /* 弹性布局 */
    justify-content: space-between; /* 两端对齐 */
    align-items: center; /* 垂直居中 */
    padding: 30rpx 0 20rpx; /* 上下内边距 */
}

/* 区域标题样式 */
.section-title {
    font-size: 32rpx; /* 字体大小 */
    font-weight: bold; /* 粗体 */
    color: #333; /* 深灰色文字 */
}

/* 信息内容区域样式 */
.info-content {
    padding-bottom: 30rpx; /* 底部内边距 */
}

/* 信息文本样式 */
.info-text {
    font-size: 28rpx; /* 字体大小 */
    color: #666; /* 中灰色文字 */
    line-height: 1.6; /* 行高 */
    display: block; /* 块级显示 */
    margin-bottom: 20rpx; /* 底部外边距 */
}

/* 要点列表样式 */
.bullet-points .point-item {
    display: flex; /* 弹性布局 */
    margin-bottom: 15rpx; /* 底部外边距 */
}

/* 要点符号样式 */
.bullet-points .point-item .bullet {
    font-size: 28rpx; /* 字体大小 */
    color: #409EFF; /* 主题蓝色 */
    margin-right: 15rpx; /* 右外边距 */
    min-width: 30rpx; /* 最小宽度 */
}

/* 要点文本样式 */
.bullet-points .point-item .point-text {
    font-size: 26rpx; /* 字体大小 */
    color: #666; /* 中灰色文字 */
    flex: 1; /* 弹性填充剩余空间 */
    line-height: 1.5; /* 行高 */
}

/* 表单区域样式 */
.form-section {
    background-color: #fff; /* 白色背景 */
    margin-bottom: 20rpx; /* 底部外边距 */
    padding: 0 30rpx; /* 左右内边距 */
}

/* 疫苗列表样式 */
.vaccine-list {
    padding: 20rpx 0; /* 上下内边距 */
}

/* 疫苗项样式 */
.vaccine-item {
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中 */
    padding: 25rpx 0; /* 上下内边距 */
    border-bottom: 1rpx solid #f0f0f0; /* 底部边框 */
    position: relative; /* 相对定位 */
    background: linear-gradient(135deg, #f5f7fa 0%, #e4edf5 100%); /* 渐变背景 */
    border-radius: 15rpx; /* 圆角 */
    margin-bottom: 20rpx; /* 底部外边距 */
    box-shadow: 0 4rpx 10rpx rgba(0, 0, 0, 0.05); /* 阴影 */
    transition: all 0.3s ease; /* 过渡动画 */
}

/* 疫苗项悬停效果 */
.vaccine-item:hover {
    transform: translateY(-5rpx); /* 上移效果 */
    box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.1); /* 加深阴影 */
}

/* 选中状态的疫苗项样式 */
.vaccine-item.active {
    background: linear-gradient(135deg, #409EFF 0%, #337ecc 100%); /* 蓝色渐变背景 */
    border-radius: 15rpx; /* 圆角 */
    margin: 0 0 20rpx 0; /* 外边距调整 */
    padding: 25rpx; /* 内边距 */
    border-bottom: none; /* 移除边框 */
    box-shadow: 0 8rpx 25rpx rgba(64, 158, 255, 0.3); /* 蓝色阴影 */
}

/* 疫苗信息区域样式 */
.vaccine-info {
    flex: 1; /* 弹性填充剩余空间 */
    display: flex; /* 弹性布局 */
    flex-direction: column; /* 垂直排列 */
}

/* 疫苗名称样式 */
.vaccine-name {
    font-size: 30rpx; /* 字体大小 */
    font-weight: bold; /* 粗体 */
    margin-bottom: 5rpx; /* 底部外边距 */
    color: #333; /* 深灰色文字 */
}

/* 疫苗描述样式 */
.vaccine-desc {
    font-size: 24rpx; /* 字体大小 */
    color: #666; /* 中灰色文字 */
    margin-bottom: 5rpx; /* 底部外边距 */
}

/* 疫苗状态指示器样式 */
.vaccine-status {
    color: #409EFF; /* 主题蓝色 */
    font-size: 36rpx; /* 字体大小 */
}

/* 选中状态下疫苗名称样式 */
.vaccine-item.active .vaccine-info .vaccine-name {
    color: #fff; /* 白色文字 */
}

/* 选中状态下疫苗描述样式 */
.vaccine-item.active .vaccine-info .vaccine-desc {
    color: #e6f2ff; /* 浅蓝色文字 */
}

/* 选中状态下状态指示器样式 */
.vaccine-item.active .vaccine-status {
    color: #fff; /* 白色文字 */
}

/* 宠物表单样式 */
.pet-form {
    padding: 20rpx 0; /* 上下内边距 */
}

/* 表单项样式 */
.form-item {
    margin-bottom: 20rpx; /* 底部外边距 */
}

/* 标签样式 */
.label {
    display: block; /* 块级显示 */
    font-size: 28rpx; /* 字体大小 */
    margin-bottom: 10rpx; /* 底部外边距 */
    color: #333; /* 深灰色文字 */
}

/* 输入框样式 */
.input {
    width: 100%; /* 宽度100% */
    height: 80rpx; /* 固定高度 */
    padding: 0 20rpx; /* 左右内边距 */
    background-color: #f8f9fa; /* 浅灰色背景 */
    border-radius: 10rpx; /* 圆角 */
    font-size: 28rpx; /* 字体大小 */
    box-sizing: border-box; /* 盒模型计算 */
}

/* 选择器显示区域样式 */
.picker-display {
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中 */
    justify-content: space-between; /* 两端对齐 */
    width: 100%; /* 宽度100% */
    height: 80rpx; /* 固定高度 */
    padding: 0 20rpx; /* 左右内边距 */
    background-color: #f8f9fa; /* 浅灰色背景 */
    border-radius: 10rpx; /* 圆角 */
    font-size: 28rpx; /* 字体大小 */
    box-sizing: border-box; /* 盒模型计算 */
}

/* 选中值样式 */
.selected-value {
    color: #333; /* 深灰色文字 */
}

/* 占位符样式 */
.placeholder {
    color: #ccc; /* 浅灰色文字 */
}

/* 箭头图标样式 */
.icon-arrow-right {
    color: #ccc; /* 浅灰色 */
    font-size: 28rpx; /* 字体大小 */
}

/* 日期时间选择器样式 */
.datetime-picker {
    padding: 30rpx 0; /* 上下内边距 */
}

/* 选择器项样式 */
.picker-item {
    display: flex; /* 弹性布局 */
    justify-content: space-between; /* 两端对齐 */
    align-items: center; /* 垂直居中 */
    padding: 20rpx 0; /* 上下内边距 */
    border-bottom: 1rpx solid #f0f0f0; /* 底部边框 */
}

/* 最后一个选择器项移除边框 */
.picker-item:last-child {
    border-bottom: none; /* 无边框 */
}

/* 选择器项标签样式 */
.picker-item .label {
    font-size: 30rpx; /* 字体大小 */
    color: #333; /* 深灰色文字 */
}

/* 选择器显示区域样式 */
.picker-item .picker-display {
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中 */
}

/* 选择器选中值样式 */
.picker-item .picker-display .selected-value {
    font-size: 30rpx; /* 字体大小 */
    color: #333; /* 深灰色文字 */
}

/* 选择器占位符样式 */
.picker-item .picker-display .placeholder {
    font-size: 30rpx; /* 字体大小 */
    color: #ccc; /* 浅灰色文字 */
}

/* 选择器箭头图标样式 */
.picker-item .picker-display .icon-arrow-right {
    color: #ccc; /* 浅灰色 */
    font-size: 28rpx; /* 字体大小 */
    margin-left: 10rpx; /* 左外边距 */
}

/* 底部操作栏样式 */
.action-bar {
    position: fixed; /* 固定定位 */
    bottom: 0; /* 底部对齐 */
    left: 0; /* 左侧对齐 */
    right: 0; /* 右侧对齐 */
    display: flex; /* 弹性布局 */
    justify-content: space-between; /* 两端对齐 */
    align-items: center; /* 垂直居中 */
    padding: 20rpx 30rpx; /* 内边距 */
    background-color: #fff; /* 白色背景 */
    border-top: 1rpx solid #eee; /* 顶部边框 */
}

/* 提交按钮样式 */
.submit-btn {
    background-color: #409EFF; /* 主题蓝色背景 */
    color: #fff; /* 白色文字 */
    padding: 20rpx 50rpx; /* 内边距 */
    border-radius: 50rpx; /* 圆形边框 */
    font-size: 32rpx; /* 字体大小 */
    font-weight: bold; /* 粗体 */
}
</style>
