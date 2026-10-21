<template>
  <!-- 养宠知识页面容器 -->
    <view class="knowledge-container">
        <!-- 搜索栏区域 -->
        <view class="search-section">
            <!-- 搜索框 -->
            <view class="search-box">
                <!-- 搜索图标 -->
                <text class="iconfont icon-aixin"/>
                <!-- 搜索输入框 -->
                <input class="search-input"
                       placeholder="搜索养宠知识"
                       v-model="query.title"
                       @confirm="searchKnowledge"
                />
            </view>
        </view>

        <!-- 知识文章列表 -->
        <view class="knowledge-list">
            <view class="knowledge-item"
                  v-for="kn in knowledgeList"
                  :key="kn.knowledgeId"
                  @click="toDetail(kn.knowledgeId)"
            >
                <!-- 文章图标区域 -->
                <view class="knowledge-icon">
                    <text class="iconfont icon-book" style="font-size: 70px"/>
                </view>
                <!-- 文章内容区域 -->
                <view class="knowledge-content">
                    <text class="knowledge-title">{{ kn.title }}</text>
                    <text class="knowledge-summary">{{ getContent(kn.content) }}</text>
                    <view class="knowledge-meta">
                        <text>{{ kn.createTime }}</text>
                    </view>
                </view>
            </view>
        </view>
    </view>
</template>

<script setup>
import {ref} from 'vue'
import {listKnowledge} from "@/pages/api/hospital/knowledge";
import {onShow} from "@dcloudio/uni-app";

//查询参数
const query = ref({
    pageNum: 1,
    pageSize: 1000,
    title: null,
})

//养宠知识列表数据
const knowledgeList = ref([])

//页面显示时加载
onShow(() => {
    load()
})

//查询养宠知识列表
const load = () => {
    listKnowledge(query.value).then(res => {
        knowledgeList.value = res.rows
    })
}

//获取文章摘要
const getContent = (content) => {
    // 使用正则表达式去除HTML标签获取纯文本
    const text = content.replace(/<[^>]+>/g, '').trim()
    //截取前100个字符并且添加省略号
    return text.length > 100 ? text.substring(0, 100) + '...' : text
}

//搜索知识文章
const searchKnowledge = () => {
    load()
}

//跳转到养宠知识详情页面
const toDetail = (id) => {
  uni.navigateTo({
      url: `/pages/knowledge/detail?id=${id}`
  })
}

</script>

<style scoped>
/* 知识库页面容器样式 */
.knowledge-container {
    background-color: #f8f9fa; /* 浅灰色背景 */
    min-height: 100vh; /* 最小高度为视口高度 */
    padding-bottom: 20rpx; /* 底部内边距 */
}

/* 搜索区域样式 */
.search-section {
    padding: 20rpx 30rpx; /* 内边距 */
    background-color: #fff; /* 白色背景 */
}

/* 搜索框样式 */
.search-box {
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中 */
    background-color: #f0f2f5; /* 浅灰色背景 */
    border-radius: 30rpx; /* 圆角边框 */
    padding: 15rpx 20rpx; /* 内边距 */
}

/* 搜索图标样式 */
.icon-search {
    font-size: 32rpx; /* 图标大小 */
    color: #999; /* 灰色 */
    margin-right: 10rpx; /* 右边距 */
}

/* 搜索输入框样式 */
.search-input {
    flex: 1; /* 占据剩余空间 */
    background: transparent; /* 透明背景 */
    font-size: 28rpx; /* 字体大小 */
}

/* 知识列表容器样式 */
.knowledge-list {
    padding: 0 30rpx; /* 左右内边距 */
}

/* 知识文章项样式 */
.knowledge-item {
    display: flex; /* 弹性布局 */
    background-color: #fff; /* 白色背景 */
    border-radius: 15rpx; /* 圆角边框 */
    margin-bottom: 20rpx; /* 底部外边距 */
    padding: 20rpx; /* 内边距 */
    box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05); /* 轻微阴影效果 */
}

/* 文章图标区域样式 */
.knowledge-icon {
    width: 200rpx; /* 固定宽度 */
    height: 150rpx; /* 固定高度 */
    border-radius: 10rpx; /* 圆角边框 */
    margin-right: 20rpx; /* 右边距 */
    background-color: #eecefc; /* 浅蓝色背景 */
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中 */
    justify-content: center; /* 水平居中 */
}

/* 文章内容区域样式 */
.knowledge-content {
    flex: 1; /* 占据剩余空间 */
    display: flex; /* 弹性布局 */
    flex-direction: column; /* 垂直排列 */
    justify-content: space-between; /* 两端对齐 */
}

/* 文章标题样式 */
.knowledge-title {
    font-size: 30rpx; /* 字体大小 */
    font-weight: bold; /* 粗体 */
    color: #333; /* 深灰色 */
    margin-bottom: 10rpx; /* 底部外边距 */
    display: -webkit-box; /* Webkit盒子模型（用于多行文本截断） */
    -webkit-box-orient: vertical; /* 垂直方向排列 */
    -webkit-line-clamp: 2; /* 最多显示2行 */
    overflow: hidden; /* 溢出隐藏 */
}

/* 文章摘要样式 */
.knowledge-summary {
    font-size: 26rpx; /* 字体大小 */
    color: #666; /* 中灰色 */
    margin-bottom: 10rpx; /* 底部外边距 */
    display: -webkit-box; /* Webkit盒子模型 */
    -webkit-box-orient: vertical; /* 垂直方向排列 */
    -webkit-line-clamp: 2; /* 最多显示2行 */
    overflow: hidden; /* 溢出隐藏 */
}

/* 文章元信息样式（创建时间等） */
.knowledge-meta {
    display: flex; /* 弹性布局 */
    justify-content: space-between; /* 两端对齐 */
    font-size: 24rpx; /* 字体大小 */
    color: #999; /* 浅灰色 */
}

/* 空状态容器样式 */
.empty-state {
    display: flex; /* 弹性布局 */
    flex-direction: column; /* 垂直排列 */
    align-items: center; /* 水平居中 */
    padding-top: 200rpx; /* 顶部内边距 */
}

/* 空状态图标样式 */
.empty-icon {
    width: 200rpx; /* 图标宽度 */
    height: 200rpx; /* 图标高度 */
    margin-bottom: 30rpx; /* 底部外边距 */
}

/* 空状态文字样式 */
.empty-text {
    font-size: 30rpx; /* 字体大小 */
    color: #999; /* 浅灰色 */
}
</style>
