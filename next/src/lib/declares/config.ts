export interface FileMetadata {
    name: string;
    path: string;
}
export interface Cacheable {
    key: string;
}
export interface Category extends FileMetadata, Cacheable {
    suffix: string[];
    books: Book[]
}
export interface Book extends FileMetadata, Cacheable {
    suffix: string[];
    menu?: BookData[];
}
export interface BookData extends FileMetadata {
    isDir: boolean
    suffix: string;
    children: BookData[];
}
export interface Doc extends FileMetadata {
    suffix: string;

}
export interface Media extends FileMetadata {
    url: string;
}

export interface ConfigFile {
    categories: Category[]

}

export interface MetadataCache {
    config: ConfigFile
    bookCache: any
    categoryCache: any
    docCache: any
}