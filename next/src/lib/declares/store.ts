import {Dispatch} from "react";

export declare class StoreData {
    dispatch: Dispatch<{ payload: any }>
}



export declare class Cache {
    key: string
    value: string
}


export declare class GlobalStoreData extends StoreData {

    categories: CategoryCache[]

}

export declare class CategoryCache extends Cache {
    books: BookCache[]
}


export declare class BookCache extends Cache {


}
export declare class BookDataCache extends Cache {

    children: BookDataCache[]

}