<template>
  <!-- 页面容器 -->
    <view class="records-container">
        <!-- 有记录时的列表显示 -->
        <view class="records-list" v-if="reservationList.length > 0">
            <!-- 记录项 -->
            <view class="record-item"
                  v-for="record in reservationList"
                  :key="record.reservationId"
                  @click="toDetail(record.reservationId)"
            >
                <view class="record-header">
                    <view class="record-type-id">
                        <!-- 预约类型 -->
                        <text class="record-type"
                              :class="record.type === '疫苗预约' ? 'vaccine-type' : 'appointment-type'">
                            {{ record.type }}
                        </text>
                        <!-- 预约单号 -->
                        <text class="record-id">{{ record.reservationId }}</text>
                    </view>
                    <!-- 预约状态 -->
                    <text class="record-status completed" v-if="record.status === '已完成'">
                        {{ record.status }}
                    </text>
                    <text class="record-status cancelled" v-if="record.status === '已取消'">
                        {{ record.status }}
                    </text>
                    <text class="record-status pending" v-if="record.status === '待就诊'">
                        {{ record.status }}
                    </text>
                </view>

                <!-- 记录内容区域 -->
                <view class="record-content">
                    <!-- 宠物信息 -->
                    <view class="pet-info">
                        <text class="pet-name">{{ record.petName }}</text>
                        <text class="pet-breed" style="margin-right: 20rpx">{{ record.petBread }}</text>
                        <text class="pet-name">{{ record.petAge }}岁</text>
                    </view>

                    <!-- 医生信息 -->
                    <view class="doctor-info" v-if="record.type === '普通预约'">
                        <!-- 医生照片 -->
                        <image :src="baseUrl + record.photo" class="doctor-photo"/>
                        <view class="doctor-name">{{ record.name }}</view>
                        <view class="doctor-name">{{ record.title }}</view>
                    </view>

                    <!-- 疫苗预约时的显示内容 -->
                    <view class="doctor-info" v-else>
                        <view class="vaccine-icon">
                            <!-- 疫苗图标 -->
                            <text class="iconfont icon-law_"></text>
                        </view>
                        <view class="doctor-details">
                            <text class="doctor-name">疫苗接种</text>
                            <text class="doctor-title">专业疫苗服务</text>
                        </view>
                    </view>

                    <!-- 预约时间 -->
                    <view class="appointment-time">
                        <text class="time-label">预约时间:</text>
                        <text class="time-value">{{ record.date }} {{ record.time }}</text>
                    </view>
                </view>

                <!-- 创建时间和取消预约按钮 -->
                <view class="record-footer">
                    <text class="create-time">预约单创建时间: {{ record.createTime }}</text>
                    <view class="record-actions" v-if="record.status === '待就诊'">
                        <button class="cancel-btn" @click.stop="cancelRecord(record)">取消预约</button>
                    </view>
                </view>

            </view>
        </view>

        <!-- 无记录时的空状态显示 -->
        <view class="empty-records" v-else>
            <text class="empty-text">暂无预约记录</text>
        </view>
    </view>
</template>

<script setup>
import {ref} from 'vue'
import {onShow} from "@dcloudio/uni-app";
import {selectMyReservationList, updateReservation} from "@/pages/api/hospital/reservation";
import config from '@/config'

//基础后端URL
const baseUrl = config.baseUrl

//预约记录数据
const reservationList = ref([])

//跳转到预约详情
const toDetail = (reservationId) => {
    uni.navigateTo({
        url: `/pages/records/detail?id=${reservationId}`
    })
}

//取消预约
const cancelRecord = (record) => {
    //构建表单对象
    const form = {
        reservationId: record.reservationId,
        status: '已取消'
    }
    //显示确认对话框
    uni.showModal({
        title: '确认取消',
        content: '确定要取消该预约单吗?',
        success: function (res) {
            //如果用户点击了确认
            if (res.confirm) {
                updateReservation(form).then(res => {
                    //更新本地记录状态
                    record.status = '已取消'
                    //显示成功提示
                    uni.showToast({
                        title: '取消预约成功',
                        icon: 'success'
                    })
                })
            }
        }
    })
}

//页面显示时调用
onShow(() => {
    selectMyReservationList().then(res => {
        reservationList.value = res.data
    })
})

</script>

<style scoped>
/* 记录容器样式 */
.records-container {
    background-color: #f8f9fa; /* 浅灰色背景 */
    min-height: 100vh; /* 最小高度为整个视口高度 */
}

/* 记录列表容器样式 */
.records-list {
    padding: 20rpx; /* 内边距 */
}

/* 单条记录项样式 */
.record-item {
    background-color: #fff; /* 白色背景 */
    border-radius: 15rpx; /* 圆角边框 */
    margin-bottom: 20rpx; /* 底部外边距 */
    padding: 20rpx; /* 内边距 */
    box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05); /* 轻微阴影效果 */
}

/* 记录头部样式 */
.record-header {
    display: flex; /* 弹性布局 */
    justify-content: space-between; /* 两端对齐 */
    align-items: center; /* 垂直居中 */
    padding-bottom: 15rpx; /* 底部内边距 */
    border-bottom: 1rpx solid #f0f0f0; /* 底部边框线 */
}

/* 类型和ID容器样式 */
.record-type-id {
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中 */
    gap: 10rpx; /* 子元素间距 */
}

/* 预约类型通用样式 */
.record-type {
    font-size: 24rpx; /* 字体大小 */
    padding: 5rpx 10rpx; /* 内边距 */
    border-radius: 8rpx; /* 圆角 */
    color: #fff; /* 白色文字 */
}

/* 普通预约类型样式 */
.record-type.appointment-type {
    background-color: #409EFF; /* 蓝色背景 */
}

/* 疫苗预约类型样式 */
.record-type.vaccine-type {
    background-color: #67C23A; /* 绿色背景 */
}

/* 预约ID样式 */
.record-id {
    font-size: 28rpx; /* 字体大小 */
    font-weight: bold; /* 粗体 */
    color: #333; /* 深灰色文字 */
}

/* 预约状态通用样式 */
.record-status {
    font-size: 24rpx; /* 字体大小 */
    padding: 5rpx 15rpx; /* 内边距 */
    border-radius: 20rpx; /* 圆形边框 */
}

/* 已完成状态样式 */
.record-status.completed {
    background-color: #f0f9eb; /* 浅绿色背景 */
    color: #67c23a; /* 绿色文字 */
}

/* 已取消状态样式 */
.record-status.cancelled {
    background-color: #fef0f0; /* 浅红色背景 */
    color: #f56c6c; /* 红色文字 */
}

/* 待处理状态样式 */
.record-status.pending {
    background-color: #ecf5ff; /* 浅蓝色背景 */
    color: #409eff; /* 蓝色文字 */
}

/* 记录内容区域样式 */
.record-content {
    padding: 20rpx 0; /* 垂直内边距 */
    border-bottom: 1rpx solid #f0f0f0; /* 底部边框线 */
}

/* 宠物信息样式 */
.pet-info {
    display: flex; /* 弹性布局 */
    margin-bottom: 20rpx; /* 底部外边距 */
}

/* 宠物名称样式 */
.pet-name {
    font-size: 30rpx; /* 字体大小 */
    font-weight: bold; /* 粗体 */
    margin-right: 20rpx; /* 右侧外边距 */
}

/* 宠物品种样式 */
.pet-breed {
    font-size: 28rpx; /* 字体大小 */
    color: #666; /* 中灰色文字 */
}

/* 医生信息容器样式 */
.doctor-info {
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中 */
    margin-bottom: 20rpx; /* 底部外边距 */
}

/* 医生头像样式 */
.doctor-photo {
    width: 60rpx; /* 宽度 */
    height: 60rpx; /* 高度 */
    border-radius: 50%; /* 圆形 */
    margin-right: 15rpx; /* 右侧外边距 */
}

/* 疫苗图标容器样式 */
.vaccine-icon {
    width: 60rpx; /* 宽度 */
    height: 60rpx; /* 高度 */
    border-radius: 50%; /* 圆形 */
    margin-right: 15rpx; /* 右侧外边距 */
    background-color: #67C23A; /* 绿色背景 */
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中 */
    justify-content: center; /* 水平居中 */
    color: #fff; /* 白色图标 */
    font-size: 36rpx; /* 图标大小 */
}

/* 医生详细信息容器样式 */
.doctor-details {
    display: flex; /* 弹性布局 */
    flex-direction: column; /* 垂直排列 */
}

/* 医生姓名样式 */
.doctor-name {
    font-size: 28rpx; /* 字体大小 */
    font-weight: bold; /* 粗体 */
    margin-bottom: 5rpx; /* 底部外边距 */
    margin-right: 20rpx
}

/* 医生职称样式 */
.doctor-title {
    font-size: 24rpx; /* 字体大小 */
    color: #999; /* 浅灰色文字 */
}

/* 预约时间容器样式 */
.appointment-time {
    display: flex; /* 弹性布局 */
    flex-direction: column; /* 垂直排列 */
}

/* 时间标签样式 */
.time-label {
    font-size: 24rpx; /* 字体大小 */
    color: #999; /* 浅灰色文字 */
    margin-bottom: 5rpx; /* 底部外边距 */
}

/* 时间值样式 */
.time-value {
    font-size: 28rpx; /* 字体大小 */
    color: #333; /* 深灰色文字 */
}

/* 记录底部样式 */
.record-footer {
    padding-top: 15rpx; /* 顶部内边距 */
    display: flex; /* 弹性布局 */
    justify-content: space-between; /* 两端对齐 */
    align-items: center; /* 垂直居中 */
}

/* 创建时间样式 */
.create-time {
    font-size: 24rpx; /* 字体大小 */
    color: #999; /* 浅灰色文字 */
}

/* 取消按钮样式 */
.record-actions .cancel-btn {
    background-color: #f56c6c; /* 红色背景 */
    color: #fff; /* 白色文字 */
    border: none; /* 无边框 */
    padding: 10rpx 20rpx; /* 内边距 */
    border-radius: 25rpx; /* 圆形边框 */
    font-size: 24rpx; /* 字体大小 */
}

/* 空记录状态容器样式 */
.empty-records {
    display: flex; /* 弹性布局 */
    flex-direction: column; /* 垂直排列 */
    align-items: center; /* 水平居中 */
    padding-top: 200rpx; /* 顶部内边距 */
}

/* 空状态图标样式 */
.empty-icon {
    width: 200rpx; /* 宽度 */
    height: 200rpx; /* 高度 */
    margin-bottom: 30rpx; /* 底部外边距 */
}

/* 空状态文字样式 */
.empty-text {
    font-size: 30rpx; /* 字体大小 */
    color: #999; /* 浅灰色文字 */
    margin-bottom: 50rpx; /* 底部外边距 */
}
</style>
