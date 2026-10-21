<template>
    <view>
        <!--预约页面容器-->
        <view class="appointment-container">
            <!--预约表单-->
            <view class="appointment-form">
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

                <!--医生选择区域-->
                <view class="form-section">
                    <view class="section-header">
                        <text class="section-title">选择医生</text>
                    </view>
                    <!--医生列表-->
                    <view class="doctor-item"
                          v-for="doctor in doctorList"
                          :key="doctor.doctorId"
                          :class="{ active: selectedDoctorId === doctor.doctorId }"
                          @click="selectDoctor(doctor.doctorId)"
                    >
                        <!--医生头像-->
                        <image :src="baseUrl + doctor.photo" class="doctor-photo"/>
                        <!--医生信息-->
                        <view class="doctor-info">
                            <text class="doctor-name">{{ doctor.name }}</text>
                            <text class="doctor-title">{{ doctor.title }}</text>
                            <text class="doctor-specialty">{{ doctor.specialty }}</text>
                        </view>
                        <!--选中状态标识-->
                        <view class="doctor-status" v-if="selectedDoctorId === doctor.doctorId">
                            <text class="iconfont icon-code"/>
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

                <!--症状区域-->
                <view class="form-section">
                    <view class="section-header">
                        <text class="section-title">宠物症状</text>
                    </view>
                    <view class="problem-input">
                        <textarea v-model="symptom"
                                  placeholder="请描述宠物的问题或症状"
                                  maxlength="200"
                                  class="problem-textarea"
                        ></textarea>
                        <!--字数统计-->
                        <view class="word-count">{{ symptom.length }}/200</view>
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
        </view>
    </view>
</template>

<script setup>
import {ref} from 'vue'
import {onShow} from "@dcloudio/uni-app";
import {listDoctor} from "@/pages/api/hospital/doctor";
//导入配置文件
import config from '@/config'
import {addReservation} from "@/pages/api/hospital/reservation";

//基础后端URL
const baseUrl = config.baseUrl

//选中的医生ID
const selectedDoctorId = ref(null)

//选择的日期
const date = ref('')
//选择的时间
const time = ref('')

//症状描述
const symptom = ref('')

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

//医生列表数据
const doctorList = ref([])

//选择医生
const selectDoctor = (doctorId) => {
    selectedDoctorId.value = doctorId
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

    //验证是否选择了医生 日期 时间
    if (!selectedDoctorId.value || !date.value || !time.value) {
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
        type: '普通预约',
        petName: petInfo.value.petName,
        petBread: petInfo.value.petBread,
        petAge: petInfo.value.petAge,
        symptom: symptom.value,
        date: date.value,
        time: time.value,
        doctorId: selectedDoctorId.value,
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

//页面显示时调用
onShow(() => {
    listDoctor(query.value).then(res => {
        doctorList.value = res.rows
    })
})

</script>

<style scoped>
/* 预约页面容器样式 */
.appointment-container {
    background-color: #f8f9fa; /* 浅灰色背景 */
    min-height: 100vh; /* 最小高度为视口高度 */
    padding-bottom: 150rpx; /* 底部内边距，为操作栏留出空间 */
}

/* 预约表单样式 */
.appointment-form {
    padding: 20rpx 0; /* 上下内边距 */
}

/* 表单区块样式 */
.form-section {
    background-color: #fff; /* 白色背景 */
    margin-bottom: 20rpx; /* 底部外边距 */
    padding: 0 30rpx; /* 左右内边距 */
}

/* 区块头部样式 */
.form-section .section-header {
    display: flex; /* 弹性布局 */
    justify-content: space-between; /* 两端对齐 */
    align-items: center; /* 垂直居中 */
    padding: 30rpx 0 20rpx; /* 内边距：上 左右 下 */
}

/* 区块标题样式 */
.section-title {
    font-size: 32rpx; /* 字体大小 */
    font-weight: bold; /* 粗体 */
}

/* 宠物表单样式 */
.pet-form {
    padding: 20rpx 0; /* 上下内边距 */
}

/* 表单项样式 */
.pet-form .form-item {
    margin-bottom: 20rpx; /* 底部外边距 */
}

/* 标签样式 */
.pet-form .label {
    display: block; /* 块级元素 */
    font-size: 28rpx; /* 字体大小 */
    margin-bottom: 10rpx; /* 底部外边距 */
    color: #333; /* 深灰色文字 */
}

/* 输入框样式 */
.pet-form .input {
    width: 100%; /* 宽度100% */
    height: 80rpx; /* 固定高度 */
    padding: 0 20rpx; /* 左右内边距 */
    background-color: #f8f9fa; /* 浅灰色背景 */
    border-radius: 10rpx; /* 圆角 */
    font-size: 28rpx; /* 字体大小 */
    box-sizing: border-box; /* 盒模型计算方式 */
}

/* 医生列表样式 */
.doctor-item {
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中 */
    padding: 25rpx 0; /* 上下内边距 */
    border-bottom: 1rpx solid #f0f0f0; /* 底部边框 */
    position: relative; /* 相对定位 */
}

/* 选中状态的医生项样式 */
.doctor-item.active {
    background-color: #f8f9fa; /* 浅灰色背景 */
    border-radius: 10rpx; /* 圆角 */
    margin: 0 -20rpx; /* 左右负外边距，扩大背景区域 */
    padding: 25rpx 20rpx; /* 内边距 */
}

/* 医生头像样式 */
.doctor-photo {
    width: 80rpx; /* 固定宽度 */
    height: 80rpx; /* 固定高度 */
    border-radius: 50%; /* 圆形 */
    margin-right: 20rpx; /* 右外边距 */
}

/* 医生信息容器样式 */
.doctor-info {
    flex: 1; /* 弹性扩展 */
    display: flex; /* 弹性布局 */
    flex-direction: column; /* 垂直方向排列 */
}

/* 医生姓名样式 */
.doctor-name {
    font-size: 30rpx; /* 字体大小 */
    font-weight: bold; /* 粗体 */
    margin-bottom: 5rpx; /* 底部外边距 */
}

/* 医生职称样式 */
.doctor-title {
    font-size: 24rpx; /* 字体大小 */
    color: #409EFF; /* 主题蓝色 */
    margin-bottom: 5rpx; /* 底部外边距 */
}

/* 医生擅长领域样式 */
.doctor-specialty {
    font-size: 24rpx; /* 字体大小 */
    color: #999; /* 浅灰色 */
}

/* 医生选中状态标识样式 */
.doctor-status {
    color: #409EFF; /* 主题蓝色 */
    font-size: 36rpx; /* 字体大小 */
}

/* 时间选择器容器样式 */
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

/* 最后一个选择器项去掉底部边框 */
.picker-item:last-child {
    border-bottom: none; /* 无边框 */
}

/* 选择器标签样式 */
.picker-item .label {
    font-size: 30rpx; /* 字体大小 */
    color: #333; /* 深灰色 */
}

/* 选择器显示区域样式 */
.picker-display {
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中 */
}

/* 选中值样式 */
.selected-value {
    font-size: 30rpx; /* 字体大小 */
    color: #333; /* 深灰色 */
}

/* 占位符样式 */
.placeholder {
    font-size: 30rpx; /* 字体大小 */
    color: #ccc; /* 浅灰色 */
}

/* 箭头图标样式 */
.icon-arrow-right {
    color: #ccc; /* 浅灰色 */
    font-size: 28rpx; /* 字体大小 */
    margin-left: 10rpx; /* 左外边距 */
}

/* 问题输入区域样式 */
.problem-input {
    position: relative; /* 相对定位，用于定位字数统计 */
}

/* 问题描述文本域样式 */
.problem-textarea {
    width: 100%; /* 宽度100% */
    height: 300rpx; /* 固定高度 */
    padding: 20rpx; /* 内边距 */
    background-color: #f8f9fa; /* 浅灰色背景 */
    border-radius: 10rpx; /* 圆角 */
    font-size: 28rpx; /* 字体大小 */
    box-sizing: border-box; /* 盒模型计算方式 */
}

/* 字数统计样式 */
.word-count {
    position: absolute; /* 绝对定位 */
    bottom: 20rpx; /* 距离底部 */
    right: 20rpx; /* 距离右侧 */
    font-size: 24rpx; /* 字体大小 */
    color: #999; /* 浅灰色 */
}

/* 底部操作栏样式 */
.action-bar {
    position: fixed; /* 固定定位 */
    bottom: 0; /* 贴底 */
    left: 0; /* 贴左 */
    right: 0; /* 贴右 */
    display: flex; /* 弹性布局 */
    justify-content: flex-end; /* 右对齐 */
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
