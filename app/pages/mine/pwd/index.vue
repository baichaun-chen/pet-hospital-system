<template>
	<view class="container">

		<view class="content">
			<view class="form-container">
				<uni-forms ref="form" :value="user" labelWidth="0">
					<view class="form-card">
						<view class="form-card-title">
							<text class="title-text">密码信息</text>
						</view>

						<view class="form-item">
							<text class="label">旧密码</text>
							<uni-easyinput type="password" class="input" v-model="user.oldPassword" placeholder="请输入旧密码" />
						</view>

						<view class="form-item">
							<text class="label">新密码</text>
							<uni-easyinput type="password" class="input" v-model="user.newPassword" placeholder="请输入新密码" />
						</view>

						<view class="form-item">
							<text class="label">确认密码</text>
							<uni-easyinput type="password" class="input" v-model="user.confirmPassword" placeholder="请确认新密码" />
						</view>
					</view>
				</uni-forms>

				<view class="button-group">
					<button class="submit-btn" @click="submit">保存密码</button>
					<button class="cancel-btn" @click="goBack">取消</button>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		updateUserPwd
	} from "@/pages/api/system/user"
	import {
		onReady
	} from '@dcloudio/uni-app'
	import {
		ref,
		getCurrentInstance,
	} from 'vue'

	const {
		proxy
	} = getCurrentInstance();

	const user = ref({
		oldPassword: undefined,
		newPassword: undefined,
		confirmPassword: undefined
	});

	const rules = ref({
		oldPassword: {
			rules: [{
				required: true,
				errorMessage: '旧密码不能为空'
			}]
		},
		newPassword: {
			rules: [{
					required: true,
					errorMessage: '新密码不能为空',
				},
				{
					minLength: 6,
					maxLength: 20,
					errorMessage: '长度在 6 到 20 个字符'
				}
			]
		},
		confirmPassword: {
			rules: [{
				required: true,
				errorMessage: '确认密码不能为空'
			}, {
				validateFunction: (rule, value, data) => data.newPassword === value,
				errorMessage: '两次输入的密码不一致'
			}]
		}
	})

	onReady(() => {
		proxy.$refs.form.setRules(rules.value)
	})

	const submit = () => {
		proxy.$refs.form.validate().then(res => {
			updateUserPwd(user.value.oldPassword, user.value.newPassword).then(response => {
				proxy.$modal.msgSuccess("修改成功")
				setTimeout(() => {
					goBack()
				}, 1000)
			})
		})
	}

	const goBack = () => {
		uni.navigateBack()
	}
</script>

<style lang="scss" scoped>
	page {
		background-color: #f5f5f5;
	}

	.container {
		min-height: 100vh;
		padding-bottom: 20px;
		padding-top: 20px;
	}

	.pwd-header {
		background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
		padding: 30px 20px;
		border-bottom-left-radius: 20px;
		border-bottom-right-radius: 20px;
		box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);

		.header-content {
			display: flex;
			flex-direction: column;
			align-items: center;

			.icon-container {
				width: 70px;
				height: 70px;
				border-radius: 50%;
				background: rgba(255, 255, 255, 0.2);
				display: flex;
				align-items: center;
				justify-content: center;
				margin-bottom: 15px;
				border: 3px solid rgba(255, 255, 255, 0.3);

				.pwd-icon {
					font-size: 36px;
					color: white;
				}
			}

			.header-title {
				color: white;
				font-size: 22px;
				font-weight: 500;
			}
		}
	}

	.content {
		padding: 20px;
		position: relative;
		top: -30px;
	}

	.form-container {
		.form-card {
			background: white;
			border-radius: 15px;
			padding: 20px;
			margin-bottom: 20px;
			box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);

			.form-card-title {
				border-left: 4px solid #4facfe;
				padding-left: 10px;
				margin-bottom: 20px;

				.title-text {
					font-size: 18px;
					font-weight: 600;
					color: #333;
				}
			}

			.form-item {
				margin-bottom: 20px;

				&:last-child {
					margin-bottom: 0;
				}

				.label {
					display: block;
					font-size: 15px;
					color: #333;
					margin-bottom: 10px;
					font-weight: 500;
				}

				.input {
					::v-deep .uni-easyinput__content {
						border-radius: 10px;
						border: 1px solid #e5e5e5;
					}
				}
			}
		}

		.button-group {
			display: flex;
			flex-direction: column;
			gap: 15px;
			padding: 0 10px;

			.submit-btn {
				background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
				border: none;
				color: white;
				font-size: 16px;
				font-weight: 500;
				border-radius: 10px;
				height: 45px;
				line-height: 45px;
			}

			.cancel-btn {
				background: #f8f9fa;
				border: 1px solid #e5e5e5;
				color: #666;
				font-size: 16px;
				font-weight: 500;
				border-radius: 10px;
				height: 45px;
				line-height: 45px;
			}
		}
	}
</style>
