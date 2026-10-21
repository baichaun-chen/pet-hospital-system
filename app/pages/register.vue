<template>
	<view class="register-container">
		<view class="register-header">
			<image class="logo" :src="globalConfig.appInfo.logo" mode="aspectFit"></image>
			<view class="title">注册</view>
			<view class="subtitle">创建您的账户</view>
		</view>

		<view class="register-form">
			<view class="form-item">
				<view class="input-wrapper">
					<view class="iconfont icon-user input-icon"></view>
					<input
						v-model="registerForm.username"
						class="input"
						type="text"
						placeholder="请输入账号"
						maxlength="30"
					/>
				</view>
			</view>

			<view class="form-item">
				<view class="input-wrapper">
					<view class="iconfont icon-password input-icon"></view>
					<input
						v-model="registerForm.password"
						type="password"
						class="input"
						placeholder="请输入密码"
						maxlength="20"
					/>
				</view>
			</view>

			<view class="form-item">
				<view class="input-wrapper">
					<view class="iconfont icon-password input-icon"></view>
					<input
						v-model="registerForm.confirmPassword"
						type="password"
						class="input"
						placeholder="请再次输入密码"
						maxlength="20"
					/>
				</view>
			</view>

			<view class="form-item" v-if="captchaEnabled">
				<view class="captcha-wrapper">
					<view class="input-wrapper captcha-input">
						<view class="iconfont icon-code input-icon"></view>
						<input
							v-model="registerForm.code"
							type="number"
							class="input"
							placeholder="请输入验证码"
							maxlength="4"
						/>
					</view>
					<view class="captcha-code" @click="getCode">
						<image :src="codeUrl" class="captcha-img" mode="aspectFit"></image>
					</view>
				</view>
			</view>

			<view class="form-item">
				<button @click="handleRegister" class="register-btn">注册</button>
			</view>
		</view>

		<view class="register-footer">
			<view class="login-link">
				<text class="text">已有账号？</text>
				<text @click="handleUserLogin" class="link">立即登录</text>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance
	} from 'vue'
	import {
		getCodeImg,
		register as registerRequset
	} from '@/api/login'

	import useUserStore from '@/store/modules/user'

	const {
		proxy
	} = getCurrentInstance();

	const userStore = useUserStore();

	const codeUrl = ref('');
	const captchaEnabled = ref(false);
	const globalConfig = ref(getApp().globalData.config);
	const registerForm = ref({
		username: "",
		password: "",
		confirmPassword: "",
		code: "",
		uuid: ''
	});
	// 用户登录
	const handleUserLogin = () => {
		proxy.$tab.redirectTo('/pages/login')
	}
	// 获取图形验证码
	const getCode = () => {
		getCodeImg().then(res => {
			captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
			if (captchaEnabled.value) {
				codeUrl.value = 'data:image/gif;base64,' + res.img
				registerForm.value.uuid = res.uuid
			}
		})
	}
	// 注册方法
	const handleRegister = async () => {
		if (registerForm.value.username === "") {
			proxy.$modal.msgError("请输入您的账号")
		} else if (registerForm.value.password === "") {
			proxy.$modal.msgError("请输入您的密码")
		} else if (registerForm.value.confirmPassword === "") {
			proxy.$modal.msgError("请再次输入您的密码")
		} else if (registerForm.value.password !== registerForm.value.confirmPassword) {
			proxy.$modal.msgError("两次输入的密码不一致")
		} else if (registerForm.value.code === "" && captchaEnabled.value) {
			proxy.$modal.msgError("请输入验证码")
		} else {
			proxy.$modal.loading("注册中，请耐心等待...")
			register()
		}
	}
	// 用户注册
	const register = async () => {
		registerRequset(registerForm.value).then(res => {
			proxy.$modal.closeLoading()
			proxy.$modal.confirm("恭喜你，您的账号 " + registerForm.value.username + " 注册成功！").then(res => {
				if (res.confirm) {
					uni.redirectTo({
						url: `/pages/login`
					});
				}
			})
		}).catch(() => {
			if (captchaEnabled.value) {
				getCode()
			}
		})
	}
	// 注册成功后，处理函数
	const registerSuccess = (result) => {
		// 设置用户信息
		userStore.getInfo().then(res => {
			proxy.$tab.reLaunch('/pages/index')
		})
	}
	getCode();
</script>

<style lang="scss">
	page {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		min-height: 100vh;
	}

	.register-container {
		padding: 0 30rpx;

		.register-header {
			text-align: center;
			padding: 120rpx 0 80rpx;

			.logo {
				width: 160rpx;
				height: 160rpx;
				border-radius: 20rpx;
				margin: 0 auto 30rpx;
				background: white;
				padding: 20rpx;
				box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.1);
			}

			.title {
				font-size: 40rpx;
				font-weight: 600;
				color: white;
				margin-bottom: 15rpx;
			}

			.subtitle {
				font-size: 28rpx;
				color: rgba(255, 255, 255, 0.9);
			}
		}

		.register-form {
			background: white;
			border-radius: 20rpx;
			padding: 40rpx;
			box-shadow: 0 8rpx 40rpx rgba(0, 0, 0, 0.15);

			.form-item {
				margin-bottom: 30rpx;

				.input-wrapper {
					display: flex;
					align-items: center;
					background: #f8f9fa;
					border-radius: 15rpx;
					padding: 20rpx 25rpx;

					.input-icon {
						font-size: 36rpx;
						color: #6c757d;
						margin-right: 15rpx;
					}

					.input {
						flex: 1;
						font-size: 28rpx;
						color: #333;
						background: transparent;
					}
				}

				.captcha-wrapper {
					display: flex;
					align-items: center;
					gap: 20rpx;

					.captcha-input {
						flex: 1;
					}

					.captcha-code {
						width: 220rpx;
						height: 70rpx;
						border-radius: 15rpx;
						overflow: hidden;
						background: #f8f9fa;

						.captcha-img {
							width: 100%;
							height: 100%;
						}
					}
				}

				.register-btn {
					width: 100%;
					height: 80rpx;
					background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
					border-radius: 15rpx;
					color: white;
					font-size: 32rpx;
					font-weight: 500;
					border: none;
					box-shadow: 0 8rpx 20rpx rgba(102, 126, 234, 0.3);
				}
			}
		}

		.register-footer {
			text-align: center;
			padding: 40rpx 0;

			.login-link {
				.text {
					font-size: 26rpx;
					color: rgba(255, 255, 255, 0.8);
				}

				.link {
					font-size: 26rpx;
					color: white;
					margin-left: 10rpx;
					text-decoration: underline;
				}
			}
		}
	}
</style>
