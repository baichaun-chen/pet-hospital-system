import config from "./config";
const path = require('path');
import fs from 'fs-extra';
import {
	defineConfig
} from 'vite';
import uni from '@dcloudio/vite-plugin-uni';

const useSeverSvg = config.useSeverSvg;
let svgTimeout = config.svgTimeout;
const svgDir = path.join(
	__dirname,
	'static/icons/svg'
)

//如果启用了服务器的svg本地的就不用了
function removeSvg() {
	const svgDistDir = path.join(
		__dirname,
		'unpackage/dist',
		process.env.NODE_ENV === 'production' ? 'build' : 'dev',
		process.env.UNI_PLATFORM === 'app' ? 'app-plus' : process.env.UNI_PLATFORM,
		'static/icons/svg'
	)
	fs.pathExists(svgDistDir).then(exists => {
		if (exists) {
			fs.remove(svgDistDir).then(res => {
				console.info(svgDistDir + "已经移除！");
			}).catch(res => {
				console.error(svgDistDir + "移除失败！");
				console.error(res);
			})
		} else {
			svgTimeout--;
			if (svgTimeout <= 0) {
				console.error("编译时间超时！不再等待删除" + svgDistDir);
				return
			}
			setTimeout(removeSvg, 1000)
		}
	}).catch(() => {
		console.error(svgDistDir + "状态获取失败！");
	})
}

//如果启用了服务器的svg本地的就不用了
if (useSeverSvg && process.env.UNI_PLATFORM != 'h5') {
	fs.pathExists(svgDir).then(exists => {
		if (exists) {
			removeSvg();
		}
	})
}

export default defineConfig({
	plugins: [
		uni(),
	],
	build: {
	},
});
