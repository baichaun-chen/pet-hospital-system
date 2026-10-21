<template>
    <view v-if="params.url">
        <web-view :webview-styles="webviewStyles" :src="`${params.url}`"></web-view>
    </view>
</template>

<script setup>
import {onLoad} from "@dcloudio/uni-app"
import {getCurrentInstance, ref,} from 'vue'

const {
    proxy
} = getCurrentInstance();

const params = ref({url: '', title: ''})
const webviewStyles = ref({
    progress: {
        color: "#FF3333"
    }
})

onLoad((event) => {
    // console.log(event);
    params.value = event
    if (event.title) {
        uni.setNavigationBarTitle({
            title: event.title
        })
    }
})
</script>
