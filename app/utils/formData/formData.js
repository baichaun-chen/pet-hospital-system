import {
	mimeMap
} from './mimeMap'

export function FormData() {
	let fileManager = uni.getFileSystemManager();
	let data = {};
	let files = [];

	this.append = (name, value) => {
		data[name] = value;
		return true;
	}

	this.appendFile = async (name, path, fileName) => {
		// #ifndef APP
		let buffer = fileManager.readFileSync(path);
		console.log(buffer);
		if (Object.prototype.toString.call(buffer).indexOf("ArrayBuffer") < 0) {
			return false;
		}

		if (!fileName) {
			fileName = getFileNameFromPath(path);
		}

		files.push({
			name: name,
			buffer: buffer,
			fileName: fileName
		});
		// #endif
		// #ifdef APP || H5
		try {
			const file = await readFileSync(name, path, fileName);
			// 在这里处理文件内容
			files.push(file);
		} catch (error) {
			console.error('读取文件时出错:', error);
		}
		// #endif
		return true;
	}

	this.getData = () => {
		return convert(data, files)
	}
}

function readFileSync(name, path, fileName) {
	return new Promise((resolve, reject) => {
		let buffer = []
		uni.saveFile({
			tempFilePath: path,
			success: function(res) {
				var savedFilePath = res.savedFilePath;
				plus.io.resolveLocalFileSystemURL(savedFilePath, function(entry) {
					entry.file(function(file) {
						var fileReader = new plus.io.FileReader();
						fileReader.readAsDataURL(file)
						fileReader.onloadend = function(evt) {
							console.log(evt);
							if (evt.target.readyState === 2) { // 检查读取是否完成
								if (evt.target.error) {
									reject(evt.target.error)
								} else {
									var fileContent = evt.target.result; // 获取文件内容
									// 这里可以对fileContent进行进一步处理
									buffer = base64ToArrayBuffer(fileContent.split(",")[1]);
									uni.removeSavedFile({
										filePath: savedFilePath
									})
									if (Object.prototype.toString.call(buffer).indexOf("ArrayBuffer") < 0) {
										return false;
									}

									if (!fileName) {
										fileName = getFileNameFromPath(savedFilePath);
									}
									resolve({
										name: name,
										"buffer": buffer,
										fileName: fileName
									});
								}
							}
						}
					})
				})
			},
			fail: function(error) {
				reject(error)
			}
		})
	});
}

function base64ToArrayBuffer(base64) {
    // 创建一个表示Base64编码数据的Blob对象
    const binaryString = atob(base64);
    const length = binaryString.length;
    const bytes = new Uint8Array(length);
    for (let i = 0; i < length; i++) {
        bytes[i] = binaryString.charCodeAt(i);
    }
    return bytes.buffer;
}

function getFileNameFromPath(path) {
	let idx = path.lastIndexOf("/");
	return path.substr(idx + 1);
}

function convert(data, files) {
	let boundaryKey = 'Boundary' + randString(); // 数据分割符，一般是随机的字符串
	let boundary = '--' + boundaryKey;
	let endBoundary = boundary + '--';

	let postArray = [];
	//拼接参数
	if (data && Object.prototype.toString.call(data) == "[object Object]") {
		for (let key in data) {
			postArray = postArray.concat(formDataArray(boundary, key, data[key]));
		}
	}
	//拼接文件
	if (files && Object.prototype.toString.call(files) == "[object Array]") {
		for (let i in files) {
			let file = files[i];
			postArray = postArray.concat(formDataArray(boundary, file.name, file.buffer, file.fileName));
		}
	}
	//结尾
	let endBoundaryArray = [];
	endBoundaryArray.push(...endBoundary.toUtf8Bytes());
	postArray = postArray.concat(endBoundaryArray);
	return {
		contentType: 'multipart/form-data; boundary=' + boundaryKey,
		buffer: new Uint8Array(postArray).buffer
	}
}

function randString() {
	var result = '';
	var chars = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'
	for (var i = 17; i > 0; --i) result += chars[Math.floor(Math.random() * chars.length)];
	return result;
}

function formDataArray(boundary, name, value, fileName) {
	let dataString = '';
	let isFile = !!fileName;

	dataString += boundary + '\r\n';
	dataString += 'Content-Disposition: form-data; name="' + name + '"';
	if (isFile) {
		dataString += '; filename="' + fileName + '"' + '\r\n';
		dataString += 'Content-Type: ' + getFileMime(fileName) + '\r\n\r\n';
	} else {
		dataString += '\r\n\r\n';
		dataString += value;
	}

	var dataArray = [];
	dataArray.push(...dataString.toUtf8Bytes());

	if (isFile) {
		let fileArray = new Uint8Array(value);
		dataArray = dataArray.concat(Array.prototype.slice.call(fileArray));
	}
	dataArray.push(..."\r".toUtf8Bytes());
	dataArray.push(..."\n".toUtf8Bytes());

	return dataArray;
}

function getFileMime(fileName) {
	let idx = fileName.lastIndexOf(".");
	let mime = mimeMap[fileName.substr(idx)];
	return mime ? mime : "application/octet-stream"
}

String.prototype.toUtf8Bytes = function() {
	var str = this;
	var bytes = [];
	for (var i = 0; i < str.length; i++) {
		bytes.push(...str.utf8CodeAt(i));
		if (str.codePointAt(i) > 0xffff) {
			i++;
		}
	}
	return bytes;
}

String.prototype.utf8CodeAt = function(i) {
	var str = this;
	var out = [],
		p = 0;
	var c = str.charCodeAt(i);
	if (c < 128) {
		out[p++] = c;
	} else if (c < 2048) {
		out[p++] = (c >> 6) | 192;
		out[p++] = (c & 63) | 128;
	} else if (
		((c & 0xFC00) == 0xD800) && (i + 1) < str.length &&
		((str.charCodeAt(i + 1) & 0xFC00) == 0xDC00)) {
		// Surrogate Pair
		c = 0x10000 + ((c & 0x03FF) << 10) + (str.charCodeAt(++i) & 0x03FF);
		out[p++] = (c >> 18) | 240;
		out[p++] = ((c >> 12) & 63) | 128;
		out[p++] = ((c >> 6) & 63) | 128;
		out[p++] = (c & 63) | 128;
	} else {
		out[p++] = (c >> 12) | 224;
		out[p++] = ((c >> 6) & 63) | 128;
		out[p++] = (c & 63) | 128;
	}
	return out;
};