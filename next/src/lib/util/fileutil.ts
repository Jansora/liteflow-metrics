const fs = require('fs');
const path = require('path');

export function readDirectories(directory: string) {

    try {
        const files = fs.readdirSync(directory);

        // 遍历文件列表
        for (const file of files) {
            const filePath = path.join(directory, file);

            // 获取文件/目录的状态信息
            const stats = fs.statSync(filePath);

            // 输出文件名和创建时间
            console.log(`${file} - 创建时间: ${stats.birthtime}`);
        }

        return files;

    } catch (err) {
        console.error('读取目录时发生错误:', err);
    }
}