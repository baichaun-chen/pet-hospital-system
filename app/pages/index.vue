<template>
    <!-- 页面容器 -->
    <view class="container">
        <!-- 顶部导航栏 -->
        <view class="navbar">
            <view class="location">
                <text class="iconfont icon-code"/>
                <text class="location-text">丫丫宠物医院</text>
            </view>
        </view>

        <!-- 轮播图区域 -->
        <view class="swiper-section">
            <swiper class="swiper" indicator-dots autoplay interval="5000" duration="1000" circular>
                <!-- 遍历显示轮播图数据 -->
                <swiper-item v-for="banner in bannerList" :key="banner.bannerId">
                    <!-- 轮播图图片 -->
                    <image :src="baseUrl + banner.image" mode="aspectFill" class="banner-image"/>
                </swiper-item>
            </swiper>
        </view>

        <!-- 快捷服务区域 -->
        <view class="services-section">
            <!-- 区域标题 -->
            <view class="section-title">
                <text>快捷服务</text>
            </view>
            <!-- 快捷服务选项 -->
            <view class="services-grid">
                <!-- 在线预约 -->
                <view class="service-item" @click="goToService('reservation')">
                    <view class="service-icon bg-blue">
                        <text class="iconfont icon-zaixianyuyue"/>
                    </view>
                    <text class="service-text">在线预约</text>
                </view>
                <!-- 疫苗接种 -->
                <view class="service-item" @click="goToService('vaccine')">
                    <view class="service-icon bg-red">
                        <text class="iconfont icon-law_"/>
                    </view>
                    <text class="service-text">疫苗接种</text>
                </view>
                <!-- 养宠知识 -->
                <view class="service-item" @click="goToService('knowledge')">
                    <view class="service-icon bg-cyan">
                        <text class="iconfont icon-chongwuziliao"/>
                    </view>
                    <text class="service-text">养宠知识</text>
                </view>
                <!-- AI问诊 -->
                <view class="service-item" @click="goToService('chat')">
                    <view class="service-icon bg-pink">
                        <text class="iconfont icon-chongwuquan-"/>
                    </view>
                    <text class="service-text">AI问诊</text>
                </view>
            </view>
        </view>

        <!-- 医院介绍区域 -->
        <view class="hospital-section">
            <!-- 区域标题 -->
            <view class="section-title">
                <text>医院介绍</text>
            </view>
            <view class="hospital-info">
                <view class="hospital-details">
                    <!-- 医院描述文字 -->
                    <text class="hospital-desc">
                        丫丫宠物医院是一家专业的宠物医疗机构，拥有先进的诊疗设备和经验丰富的兽医团队。
                        我们提供全面的宠物医疗服务，包括常规体检、疫苗接种、外科手术、美容护理等。
                        医院秉承"关爱宠物，服务至上"的理念，为每一位宠物主人提供最优质的服务。
                    </text>
                    <!-- 医院数据统计 -->
                    <view class="hospital-stats">
                        <!-- 医师数量 -->
                        <view class="stat-item">
                            <text class="stat-number">10+</text>
                            <text class="stat-label">专业医师</text>
                        </view>
                        <!-- 科室数量 -->
                        <view class="stat-item">
                            <text class="stat-number">8</text>
                            <text class="stat-label">诊疗科室</text>
                        </view>
                        <!-- 服务宠物数量 -->
                        <view class="stat-item">
                            <text class="stat-number">5000+</text>
                            <text class="stat-label">服务宠物</text>
                        </view>
                    </view>
                </view>
            </view>
        </view>

    </view>
</template>

<script setup>
import {ref} from 'vue'
import {onShow} from "@dcloudio/uni-app";
import {listBanner} from "@/pages/api/hospital/banner";
//导入配置文件
import config from '@/config'

//基础后端URL
const baseUrl = config.baseUrl

//轮播图查询参数
const bannerQuery = ref({
    pageNum: 1, //页码
    pageSize: 10, //每页数量
})

//轮播图数据
const bannerList = ref([])

//页面显示时执行
onShow(() => {
    listBanner(bannerQuery.value).then(res => {
        bannerList.value = res.rows
    })
})

//跳转到对应的页面
const goToService = (service) => {
    uni.navigateTo({
        url: `/pages/${service}/index`
    })
}

</script>

<style scoped>
/* 页面容器样式 */
.container {
    background-color: #f8f9fa;  /* 浅灰色背景 */
    padding-bottom: 20rpx;      /* 底部内边距 */
}

/* 导航栏样式 */
.navbar {
    display: flex;              /* 弹性布局 */
    justify-content: space-between; /* 两端对齐 */
    align-items: center;        /* 垂直居中 */
    padding: 20rpx 30rpx;       /* 内边距 */
    background-color: #fff;     /* 白色背景 */
}

/* 位置信息样式 */
.location {
    display: flex;              /* 弹性布局 */
    align-items: center;        /* 垂直居中 */
}

/* 位置文字样式 */
.location-text {
    margin-left: 10rpx;         /* 左边距 */
    font-size: 28rpx;           /* 字体大小 */
}

/* 轮播图区域样式 */
.swiper-section {
    padding: 0 30rpx 20rpx;     /* 内边距：上 左右 下 */
}

/* 轮播图样式 */
.swiper {
    height: 350rpx;             /* 固定高度 */
    border-radius: 20rpx;       /* 圆角 */
    overflow: hidden;           /* 隐藏溢出内容 */
}

/* 轮播图图片样式 */
.banner-image {
    width: 100%;                /* 宽度100% */
    height: 100%;               /* 高度100% */
}

/* 区域标题样式 */
.section-title {
    font-size: 32rpx;           /* 字体大小 */
    font-weight: bold;          /* 粗体 */
    padding: 20rpx 30rpx 10rpx; /* 内边距：上 左右 下 */
}

/* 快捷服务区域样式 */
.services-section {
    background-color: #fff;     /* 白色背景 */
    margin-bottom: 20rpx;       /* 底部外边距 */
}

/* 服务网格布局样式 */
.services-grid {
    display: flex;              /* 弹性布局 */
    flex-wrap: wrap;            /* 允许换行 */
    padding: 0 20rpx;           /* 左右内边距 */
}

/* 单个服务项样式 */
.service-item {
    width: 25%;                 /* 宽度25%，一行4个 */
    display: flex;              /* 弹性布局 */
    flex-direction: column;     /* 垂直排列 */
    align-items: center;        /* 水平居中 */
    padding: 20rpx 0;           /* 上下内边距 */
}

/* 服务图标容器样式 */
.service-icon {
    width: 80rpx;               /* 固定宽度 */
    height: 80rpx;              /* 固定高度 */
    border-radius: 50%;         /* 圆形 */
    display: flex;              /* 弹性布局 */
    align-items: center;        /* 垂直居中 */
    justify-content: center;    /* 水平居中 */
    margin-bottom: 10rpx;       /* 底部外边距 */
    color: #fff;                /* 白色文字 */
    font-size: 36rpx;           /* 图标大小 */
}

/* 服务文字样式 */
.service-text {
    font-size: 24rpx;           /* 字体大小 */
}

/* 服务图标背景色类 */
.bg-blue {
    background-color: #409EFF;  /* 蓝色背景 */
}

.bg-red {
    background-color: #F56C6C;  /* 红色背景 */
}

.bg-cyan {
    background-color: #00B5AD;  /* 青色背景 */
}

.bg-pink {
    background-color: #F56292;  /* 粉色背景 */
}

/* 医院介绍区域样式 */
.hospital-section {
    background-color: #fff;     /* 白色背景 */
    margin-bottom: 20rpx;       /* 底部外边距 */
}

/* 医院信息样式 */
.hospital-info {
    padding: 0 30rpx 20rpx;     /* 内边距：上 左右 下 */
}

/* 医院描述文字样式 */
.hospital-desc {
    font-size: 26rpx;           /* 字体大小 */
    color: #666;                /* 灰色文字 */
    line-height: 1.5;           /* 行高1.5倍 */
    display: block;             /* 块级显示 */
    margin-bottom: 20rpx;       /* 底部外边距 */
}

/* 医院数据统计区域样式 */
.hospital-stats {
    display: flex;              /* 弹性布局 */
    justify-content: space-around; /* 均匀分布 */
    text-align: center;         /* 文字居中 */
}

/* 单个统计项样式 */
.stat-item {
    /* 空规则，保留结构 */
}

/* 统计数字样式 */
.stat-number {
    display: block;             /* 块级显示 */
    font-size: 36rpx;           /* 字体大小 */
    font-weight: bold;          /* 粗体 */
    color: #409EFF;             /* 蓝色文字 */
}

/* 统计标签样式 */
.stat-label {
    font-size: 24rpx;           /* 字体大小 */
    color: #999;                /* 浅灰色文字 */
}
</style>
