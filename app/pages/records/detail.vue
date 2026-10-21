<template>
  <!-- 预约详情页面容器 -->
    <view class="record-detail-container">
        <!-- 主要内容区域 -->
        <view class="record-detail-container" v-if="record">
            <!-- 预约状态 -->
            <view class="status-section">
                <text class="status-label">预约状态</text>
                <text class="status-value completed" v-if="record.status === '已完成'">
                    {{ record.status }}
                </text>
                <text class="status-value cancelled" v-if="record.status === '已取消'">
                    {{ record.status }}
                </text>
                <text class="status-value pending" v-if="record.status === '待就诊'">
                    {{ record.status }}
                </text>
            </view>

            <!-- 预约单号 -->
            <view class="record-id-section">
                <view class="id-type">
                    <text class="type-tag"
                          :class="record.type === '疫苗预约' ? 'vaccine-type' : 'appointment-type'">
                        {{ record.type }}
                    </text>
                    <text class="id-value">{{ record.reservationId }}</text>
                </view>
            </view>

            <!-- 宠物信息区域 -->
            <view class="section">
                <view class="section-header">
                    <text class="section-title">宠物信息</text>
                </view>
                <view class="section-content">
                    <view class="info-item">
                        <text class="info-label">宠物姓名</text>
                        <text class="info-value">{{ record.petName }}</text>
                    </view>
                    <view class="info-item">
                        <text class="info-label">宠物品种</text>
                        <text class="info-value">{{ record.petBread }}</text>
                    </view>
                    <view class="info-item">
                        <text class="info-label">宠物年龄</text>
                        <text class="info-value">{{ record.petAge }}岁</text>
                    </view>
                </view>
            </view>

            <!-- 医生信息区域(普通预约) -->
            <view class="section" v-if="record.type === '普通预约'">
                <view class="section-header">
                    <text class="section-title">医生信息</text>
                </view>
                <view class="section-content doctor-section">
                    <!-- 医生照片 -->
                    <image :src="baseUrl + record.photo" class="doctor-avatar"/>
                    <!-- 医生信息 -->
                    <view class="doctor-info">
                        <text class="doctor-name">{{ record.name }}</text>
                        <text class="doctor-title">{{ record.title }}</text>
                    </view>
                </view>
            </view>

            <!-- 疫苗信息区域(疫苗预约) -->
            <view class="section" v-else>
                <view class="section-header">
                    <text class="section-title">服务信息</text>
                </view>
                <view class="section-content doctor-section">
                    <!-- 疫苗图标 -->
                    <view class="vaccine-icon">
                        <text class="iconfont icon-law_"></text>
                    </view>
                    <!-- 疫苗服务信息 -->
                    <view class="doctor-info">
                        <text class="doctor-name">疫苗接种服务</text>
                        <text class="doctor-title">专业疫苗服务</text>
                        <text class="doctor-department">丫丫宠物医院</text>
                    </view>
                </view>
            </view>

            <!-- 预约时间区域 -->
            <view class="section">
                <view class="section-header">
                    <text class="section-title">预约时间</text>
                </view>
                <view class="section-content">
                    <!-- 预约日期 -->
                    <view class="info-item">
                        <text class="info-label">预约日期</text>
                        <text class="info-value">{{ record.date }}</text>
                    </view>
                    <!-- 预约时间 -->
                    <view class="info-item">
                        <text class="info-label">预约时间</text>
                        <text class="info-value">{{ record.time }}</text>
                    </view>
                </view>
            </view>

            <!-- 症状描述区域 -->
            <view class="section" v-if="record.type === '普通预约'">
                <view class="section-header">
                    <text class="section-title">症状描述</text>
                </view>
                <view class="section-content">
                    <view class="symptom-description">
                        <text class="description-text">{{ record.symptom }}</text>
                    </view>
                </view>
            </view>

            <!-- 疫苗信息 -->
            <view class="section" v-if="record.type === '疫苗预约'">
                <view class="section-header">
                    <text class="section-title">疫苗信息</text>
                </view>
                <view class="section-content">
                    <view class="vaccine-item">
                        <text class="vaccine-name">{{ record.vaccineName }}</text>
                    </view>
                </view>
            </view>

        </view>
    </view>
</template>

<script setup>
import {ref} from 'vue'
import {onLoad} from "@dcloudio/uni-app";
import {getReservation} from "@/pages/api/hospital/reservation";
import config from '@/config'

//基础后端URL
const baseUrl = config.baseUrl

//预约数据
const record = ref({})

onLoad((options) => {
    getReservation(options.id).then(res => {
        record.value = res.data
    })
})

</script>

<style scoped>
/* 页面容器样式 */
.record-detail-container {
    background-color: #f8f9fa; /* 浅灰色背景 */
    min-height: 100vh; /* 最小高度为整个视口高度 */
    padding-bottom: 40rpx; /* 底部内边距 */
}

/* 状态区域样式 */
.status-section {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #fff; /* 白色背景 */
    padding: 30rpx;
    margin-bottom: 20rpx; /* 底部外边距 */
}

.status-label {
    font-size: 30rpx;
    color: #333; /* 深灰色文字 */
}

.status-value {
    font-size: 32rpx;
    font-weight: bold;
    padding: 10rpx 20rpx;
    border-radius: 20rpx; /* 圆角边框 */
}

/* 不同状态的背景色和文字颜色 */
.status-value.completed {
    background-color: #f0f9eb; /* 浅绿色背景 */
    color: #67c23a; /* 绿色文字 */
}

.status-value.cancelled {
    background-color: #fef0f0; /* 浅红色背景 */
    color: #f56c6c; /* 红色文字 */
}

.status-value.pending {
    background-color: #ecf5ff; /* 浅蓝色背景 */
    color: #409eff; /* 蓝色文字 */
}

/* 预约编号区域样式 */
.record-id-section {
    display: flex;
    justify-content: space-between;
    background-color: #fff;
    padding: 30rpx;
    margin-bottom: 20rpx;
}

.id-type {
    display: flex;
    flex-direction: column;
    gap: 10rpx; /* 子元素间距 */
}

.type-tag {
    align-self: flex-start; /* 左对齐 */
    font-size: 24rpx;
    padding: 5rpx 10rpx;
    border-radius: 8rpx;
    color: #fff; /* 白色文字 */
}

/* 不同类型标签的颜色 */
.type-tag.appointment-type {
    background-color: #409EFF; /* 蓝色背景 */
}

.type-tag.vaccine-type {
    background-color: #67C23A; /* 绿色背景 */
}

.id-value {
    font-size: 28rpx;
    color: #666; /* 中灰色文字 */
}

/* 通用区块样式 */
.section {
    background-color: #fff;
    margin-bottom: 20rpx;
}

.section-header {
    padding: 20rpx 30rpx;
    border-bottom: 1rpx solid #f0f0f0; /* 底部边框线 */
}

.section-title {
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
}

.section-content {
    padding: 20rpx 30rpx;
}

/* 信息项样式 */
.info-item {
    display: flex;
    justify-content: space-between;
    padding: 15rpx 0;
}

.info-label {
    font-size: 28rpx;
    color: #666; /* 标签文字颜色较浅 */
}

.info-value {
    font-size: 28rpx;
    color: #333; /* 值文字颜色较深 */
}

/* 医生区域样式 */
.doctor-section {
    display: flex;
    align-items: center;
}

.doctor-avatar {
    width: 100rpx;
    height: 100rpx;
    border-radius: 50%; /* 圆形头像 */
    margin-right: 30rpx;
}

/* 疫苗图标样式 */
.vaccine-icon {
    width: 100rpx;
    height: 100rpx;
    border-radius: 50%;
    margin-right: 30rpx;
    background-color: #67C23A; /* 绿色背景 */
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    font-size: 48rpx;
}

/* 医生信息样式 */
.doctor-info {
    display: flex;
    flex-direction: column;
}

.doctor-name {
    font-size: 32rpx;
    font-weight: bold;
    margin-bottom: 10rpx;
}

.doctor-title {
    font-size: 28rpx;
    color: #666;
    margin-bottom: 5rpx;
}

.doctor-department {
    font-size: 26rpx;
    color: #999; /* 浅灰色文字 */
}

/* 症状描述样式 */
.symptom-description .description-text {
    font-size: 28rpx;
    color: #333;
    line-height: 1.6; /* 行高设置，提高可读性 */
}

/* 疫苗项样式 */
.vaccine-item {
    display: flex;
    justify-content: space-between;
    padding: 15rpx 0;
    border-bottom: 1rpx solid #f0f0f0; /* 底部边框线 */
}

.vaccine-name {
    font-size: 28rpx;
    color: #333;
}

/* 最后一个疫苗项去掉边框 */
.vaccine-item:last-child {
    border-bottom: none;
}
</style>
