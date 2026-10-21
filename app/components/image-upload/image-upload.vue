<template>
	<view class="component-upload-image">
		<uni-file-picker ref="image-upload" v-model="fileList" fileMediatype="image" mode="grid" :auto-upload="true"
			:limit="limit" :file-extname="fileType" :title="'最多选择' + limit + '张图片'" @select="handleBeforeUpload"
			@success="handleUploadSuccess" @fail="handleUploadError" @delete="handleDelete" :sizeType="['original']"
			:readonly="props.readonly">
		</uni-file-picker>
		<!-- 上传提示 -->
		<view v-if="showTip">
			请上传
			<text v-if="fileSize">
				大小不超过 <text style="color: #f56c6c">{{ fileSize }}MB</text>
			</text>
			<text v-if="fileType">
				格式为 <text style="color: #f56c6c">{{ fileType.join("/") }}</text>
			</text>
			的文件
		</view>
		<xy-popup />
	</view>
</template>
<script setup>
	import {
		ref,
		toRefs,
		watch,
		getCurrentInstance,
		inject,
		computed
	} from 'vue';
	import {
		onLoad,
		onUnload,
	} from '@dcloudio/uni-app'
	import config from '@/config'
	import {
		getToken
	} from "@/utils/auth";
	import {
		isExternal
	} from "@/utils/validate";
	import upload from '@/utils/upload'

	/** 接收传入参数 */
	const props = defineProps({
		modelValue: [String, Object, Array],
		// 图片数量限制
		limit: {
			type: Number,
			default: 5,
		},
		// 大小限制(MB)
		fileSize: {
			type: Number,
			default: 5,
		},
		// 文件类型, 例如['png', 'jpg', 'jpeg']
		fileType: {
			type: Array,
			default: () => ["png", "jpg", "jpeg"],
		},
		// 是否显示提示
		isShowTip: {
			type: Boolean,
			default: true
		},
		//是否只读
		readonly: {
			type: Boolean,
			default: false
		},
	})

	const {
		proxy
	} = getCurrentInstance();
	const emit = defineEmits();
	const number = ref(0);
	const uploadList = ref([]);
	const dialogImageUrl = ref("");
	const dialogVisible = ref(false);
	const baseUrl = config.baseUrl; // 上传的图片服务器地址
	const headers = ref({
		Authorization: "Bearer " + getToken()
	});
	const fileList = ref([]);
	const showTip = computed(
		() => props.isShowTip && (props.fileType || props.fileSize)
	);

	watch(() => props.modelValue, val => {
		if (val) {
			// 首先将值转为数组
			const list = Array.isArray(val) ? val : props.modelValue.split(",");
			// 然后将数组转为对象数组
			fileList.value = list.map(item => {
				if (typeof item === "string") {
					if (item.indexOf(baseUrl) === -1 && !isExternal(item)) {
						item = {
							name: item.split('/')[item.split('/').length - 1],
							extname: item.split('.')[item.split('.').length - 1],
							url: baseUrl + item
						};
					} else {
						item = {
							name: item.split('/')[item.split('/').length - 1],
							extname: item.split('.')[item.split('.').length - 1],
							url: item
						};
					}
				}
				return item;
			});
		} else {
			fileList.value = [];
			return [];
		}
	}, {
		deep: true,
		immediate: true
	});

	// 上传前loading加载
	function handleBeforeUpload(data) {
		for (let [index, item] of data.tempFiles.entries()) {
			let file = item.file
			let isImg = false;
			if (props.fileType.length) {
				let fileExtension = "";
				if (file.name.lastIndexOf(".") > -1) {
					fileExtension = file.name.slice(file.name.lastIndexOf(".") + 1);
				}
				isImg = props.fileType.some(type => {
					if (file.fileType.indexOf(type) > -1) return true;
					if (fileExtension && fileExtension.indexOf(type) > -1) return true;
					return false;
				});
			} else {
				isImg = file.fileType.indexOf("image") > -1;
			}
			if (!isImg) {
				proxy.$modal.msgError(
					`文件格式不正确, 请上传${props.fileType.join("/")}图片格式文件!`
				);
				proxy.$refs['image-upload'].clearFiles(fileList.value.length + index - failNumber);
				failNumber++
				continue
			}
			if (props.fileSize) {
				const isLt = file.size / 1024 / 1024 < props.fileSize;
				if (!isLt) {
					proxy.$modal.msgError(`上传头像图片大小不能超过 ${props.fileSize} MB!`);
					proxy.$refs['image-upload'].clearFiles(fileList.value.length + index - failNumber);
					failNumber++
					continue
				}
			}
			number.value++;
		}
		if (number.value === 0) return false;
		proxy.$modal.loading("正在上传文件，请稍候...");
		proxy.$refs['image-upload'].upload()
		return true;
	}

	// 文件个数超出
	function handleExceed() {
		proxy.$modal.msgError(`上传文件数量不能超过 ${props.limit} 个!`);
	}

	// 上传成功回调
	function handleUploadSuccess(data) {
		//上传失败的数量
		let failNumber = 0;
		let notOverNumber = data.tempFiles.length
		for (let [index, item] of data.tempFiles.entries()) {
			upload({
				url: "/common/upload",
				name: 'file',
				file: item,
			}).then(res => {
				if (res.code === 200) {
					if (config.isCloud) {
						uploadList.value.push({
							name: res.data.name,
							extname: res.data.url.split('.')[res.data.url.split('.').length - 1],
							url: res.data.url
						});
					} else {
						uploadList.value.push({
							name: res.fileName.split('/')[res.fileName.split('/').length - 1],
							extname: res.fileName.split('.')[res.fileName.split('.').length - 1],
							url: res.fileName
						});
					}
				} else {
					number.value--;
					proxy.$modal.msgError(res.msg);
					failNumber++;
				}
				notOverNumber--
				if (notOverNumber === 0) {
					fileList.value.splice(fileList.value.length - data.tempFiles.length, data.tempFiles.length);
					uploadedSuccessfully();
				}
			}).catch(res => {
				number.value--;
				notOverNumber--
				failNumber++;
			})
		}
	}

	// 删除图片
	function handleDelete(data) {
		fileList.value.splice(data.index, 1);
		emit("update:modelValue", listToString(fileList.value));
	}

	// 上传结束处理
	function uploadedSuccessfully() {
		if (number.value > 0 && uploadList.value.length === number.value) {
			fileList.value = fileList.value.filter(f => f.url !== undefined && f.url !== '').concat(uploadList.value);
			uploadList.value = [];
			number.value = 0;
			emit("update:modelValue", listToString(fileList.value));
		}
		proxy.$modal.closeLoading();
	}

	// 上传失败
	function handleUploadError(data) {
		handleDelete(data)
		proxy.$modal.closeLoading();
		proxy.$modal.msgError("上传图片失败");
	}

	// 对象转成指定字符串分隔
	function listToString(list, separator) {
		let strs = "";
		separator = separator || ",";
		for (let i in list) {
			if (undefined !== list[i].url && list[i].url.indexOf("blob:") !== 0) {
				strs += list[i].url.replace(baseUrl, "") + separator;
			}
		}
		return strs != "" ? strs.substr(0, strs.length - 1) : "";
	}
</script>