import * as process from "process";
import {Book, BookData, Category, ConfigFile, MetadataCache} from "@/lib/declares/config";
import {hash} from "@/lib/util/utils";
import {undefined} from "zod";

const fs = require('fs');
const path = require('path');

export const metadataCache: MetadataCache = {
    // @ts-ignore
    config: null,
    bookCache: {},
    categoryCache: {},
    docCache: {}
}
export const reloadCache = () => {
    // @ts-ignore
    metadataCache.config = null
    metadataCache.bookCache = {}
    metadataCache.categoryCache = {}
    metadataCache.docCache = {}
    initConfig()
}

// @ts-ignore
export const loadConfig = (): ConfigFile => {
    // 默认读缓存
    if (metadataCache.config) {
        console.log("read cache.")
        return metadataCache.config
    }
    return initConfig()
}

// @ts-ignore
export const initConfig = (): ConfigFile => {
    // 默认读缓存
    if (metadataCache.config) {
        return metadataCache.config
    }

    const path = process.env.CONFIG_PATH || "./defaultConfig.json";

    // 同步读取 JSON 文件内容
    try {
        metadataCache.config = JSON.parse(fs.readFileSync(path, 'utf8'));
        metadataCache.config.categories.forEach(category => {
            initCategory(category)
        })
    } catch (err) {
        console.error('读取或解析配置文件时发生错误:', err);
    }
    return metadataCache.config
}

/**
 * 初始化书本
 * @param category
 */
// @ts-ignore
export const initCategory = (category: Category): void => {
    metadataCache.categoryCache[hash(category.path)] = category
    initBooks(category)
}
/**
 * 初始化书本
 * @param category
 */
// @ts-ignore
export const initBooks = (category: Category): void => {

    // @ts-ignore 重新缓存
    category.books = []

    // 同步读取 JSON 文件内容
    try {
        const files: string[] = fs.readdirSync(category.path);
        // console.log("files", files)
        // 使用map方法创建一个包含文件名和绝对路径的对象数组

        category.books = files
            .filter(file => {
                const filePath = path.join(category.path, file);
                // 隐藏文件
                if (file.startsWith(".")) {
                    return false;
                }
                if (fs.statSync(filePath).isDirectory()) {
                    return true;
                }
            })
            // @ts-ignore
            .sort((a, b) => a.localeCompare(b, undefined, {sensitivity: 'base'}))
            .map((file: string) => {

                const filePath = path.join(category.path, file);

                const book: Book = {
                    key: hash(filePath),
                    name: path.basename(file, path.extname(file)),
                    path: path.join(category.path, file),
                    suffix: category.suffix,
                    // @ts-ignore
                    menu: null,
                    // menu: loadBook(category.path, category.suffix)
                };

                metadataCache.bookCache[book.key] = book

                return book;

            });


    }
    catch (err) {
        console.error('读取或解析配置文件时发生错误:', err);
    }

}

export const initBook = (bookHash: string): Book => {

    const book : Book = metadataCache.bookCache[bookHash]

    // 默认读缓存
    if (book && 'menu' in book && !!book.menu) {
        return book
    }

    if (book) {
        book.menu = initBookData(book.path, book.suffix);
    }
    return book;
}
export const loadBook = (bookHash: string): Book => {

    // 默认读缓存
    const book : Book = metadataCache.bookCache[bookHash]
    if (book && 'menu' in book && !!book.menu) {
        return book
    }

    initBook(bookHash)

    return metadataCache.bookCache[bookHash];
}

export const loadBookData = (bookHash: string, bookDataHash: string): BookData => {

    const bookData: BookData = metadataCache.docCache[bookDataHash]

    if (!!bookData) {
        return bookData;
    }

    loadBook(bookHash);

    return metadataCache.docCache[bookDataHash];

}


export const initBookData = (dir: string, extensions: string[]): BookData[] => {

    const files: string[] = fs.readdirSync(dir);
    return files
        .filter(file => {
            const filePath = path.join(dir, file);
            // 隐藏文件
            if (file.startsWith(".")) {
                return false;
            }
            if (fs.statSync(filePath).isDirectory()) {
                return true;
            }
            const ext = path.extname(file).toLowerCase();
            return extensions.includes(ext);
        })
        // @ts-ignore
        .sort((a, b) => a.localeCompare(b, undefined, {sensitivity: 'base'}))
        .map(file => {
            const filePath: string = path.join(dir, file);
            const suffix = path.extname(file);
            const name: string = path.basename(file, suffix); // 删除后缀的文件名
            const isDir: boolean = fs.statSync(filePath).isDirectory();
            const metadata: BookData = {
                isDir, suffix,
                name, path: filePath, children: []
            }
            if (isDir) {
                metadata.children = initBookData(filePath, extensions);
            }
            metadataCache.docCache[hash(filePath)] = metadata
            return metadata;
        });
}