const crypto_module = require('crypto');


function calculateStringHash(input: string, algorithm = 'sha256'): string {
    const hash = crypto_module.createHash(algorithm);
    hash.update(input, 'utf8');
    return hash.digest('hex');
}

/**
 * 计算简单的 hash
 */
export function hash(input: string): string {

    const fullHash = calculateStringHash(input)
    // 取后8位
    return fullHash.slice(-8);
}


/**
 * 是否是文档
 */
export function isDoc(suffix: string): boolean {
    if(!suffix) {
        return false;
    }
    return [".md", ".txt", ".doc", ".docx"].includes(suffix);
}

/**
 * 是否是文档
 */
export function isPdf(suffix: string): boolean {
    if(!suffix) {
        return false;
    }
    return [".pdf"].includes(suffix);
}
/**
 * 是否是文档
 */
export function isHtml(suffix: string): boolean {
    if(!suffix) {
        return false;
    }
    return [".html"].includes(suffix);
}


export const STATIC = "__static__"