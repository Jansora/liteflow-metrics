"use client"

import React, {Context, createContext, useReducer} from 'react';

import {GlobalStoreData} from "../declares/store";


export const defaultValue: GlobalStoreData = {
    category: [],
    // @ts-ignore
    dispatch: null
};

export const GlobalStore: Context<GlobalStoreData> = createContext(defaultValue);


const reducer = (state: GlobalStoreData, action: { payload: GlobalStoreData}): GlobalStoreData => {
   return {...state, ...action.payload}
}


const GlobalStoreProvider = (props) => {

    const { initialData } = props;
    const [store, dispatch] = useReducer(reducer, { ...defaultValue, ...initialData});

    store.dispatch = dispatch

    return (
        <GlobalStore.Provider value={store}>
            {props.children}
        </GlobalStore.Provider>
    );
};
export default GlobalStoreProvider;
