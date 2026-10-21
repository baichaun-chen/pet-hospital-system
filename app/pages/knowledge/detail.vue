<template>
    <!-- 知识详情页面容器 -->
    <view class="knowledge-detail-container">
        <!-- 文章头部信息区域 -->
        <view class="knowledge-header" v-if="knowledge">
            <!-- 文章标题 -->
            <text class="knowledge-title">{{ knowledge.title }}</text>
            <view class="knowledge-meta">
                <text>{{ knowledge.createTime }}</text>
            </view>
        </view>

        <!-- 文章内容区域 -->
        <view class="knowledge-content" v-if="knowledge">
            <rich-text :nodes="knowledge.content"/>
        </view>
    </view>
</template>

<script setup>
import {ref} from 'vue'
import {onLoad} from "@dcloudio/uni-app";
import {getKnowledge} from "@/pages/api/hospital/knowledge";

//养宠知识数据
const knowledge = ref({})

//页面加载时调用
onLoad((options) => {
    getKnowledge(options.id).then(res => {
        knowledge.value = res.data
    })
})

</script>

<style scoped>
/* 知识详情页面容器样式 */
.knowledge-detail-container {
    background-color: #f8f9fa;  /* 浅灰色背景，与列表页保持一致 */
    min-height: 100vh;          /* 最小高度为视口高度，确保占满整个屏幕 */
    padding-bottom: 40rpx;      /* 底部内边距，为内容提供呼吸空间 */
}

/* 文章头部信息区域样式 */
.knowledge-header {
    background-color: #fff;     /* 白色背景，与内容区域区分 */
    padding: 30rpx;             /* 内边距 */
    margin-bottom: 20rpx;       /* 底部外边距，与内容区域分隔 */
}

/* 文章标题样式 */
.knowledge-title {
    font-size: 36rpx;           /* 较大的字体大小，突出标题重要性 */
    font-weight: bold;          /* 粗体显示，增强视觉层次 */
    color: #333;                /* 深灰色，确保良好的可读性 */
    line-height: 1.5;           /* 1.5倍行高，提高阅读舒适度 */
    margin-bottom: 20rpx;       /* 底部外边距，与元信息区域分隔 */
}

/* 文章元信息容器样式 */
.knowledge-meta {
    display: flex;              /* 弹性布局，便于水平排列多个元信息项 */
    justify-content: space-between;  /* 两端对齐，目前只有时间一项，实际可扩展作者等信息 */
    font-size: 26rpx;           /* 较小的字体大小，表示次要信息 */
    color: #999;                /* 浅灰色，表示次要信息 */
}

/* 文章内容区域样式 */
.knowledge-content {
    background-color: #fff;     /* 白色背景，与头部保持一致 */
    padding: 30rpx;             /* 内边距，为内容提供舒适的阅读空间 */
    margin-bottom: 20rpx;       /* 底部外边距 */
}

/* 富文本基础样式 */
::v-deep rich-text {
    font-size: 30rpx;           /* 正文字体大小，适中便于阅读 */
    line-height: 1.8;           /* 1.8倍行高，提供舒适的阅读行间距 */
    color: #333;                /* 深灰色正文颜色 */
}

/* 三级标题样式 */
::v-deep rich-text h3 {
    font-size: 32rpx;           /* 比正文稍大的字体大小 */
    font-weight: bold;          /* 粗体显示 */
    margin: 30rpx 0 20rpx;      /* 上下外边距：30rpx上，20rpx下，左右为0 */
    color: #409EFF;             /* 主题蓝色，突出标题重要性 */
}

/* 段落样式 */
::v-deep rich-text p {
    margin-bottom: 20rpx;       /* 段落底部外边距，分隔各个段落 */
}

/* 无序列表和有序列表容器样式 */
::v-deep rich-text ul, ::v-deep rich-text ol {
    margin: 20rpx 0;            /* 列表上下外边距 */
    padding-left: 40rpx;        /* 左内边距，为列表标记提供空间 */
}

/* 列表项样式 */
::v-deep rich-text li {
    margin-bottom: 10rpx;       /* 列表项之间的垂直间距 */
}

/* 图片样式 */
::v-deep rich-text img {
    max-width: 100%;            /* 图片最大宽度为容器宽度，确保响应式 */
    border-radius: 10rpx;       /* 圆角边框，美化图片外观 */
    margin: 20rpx 0;            /* 图片上下外边距，与周围内容分隔 */
}
</style>
