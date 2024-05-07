import React from "react";
import RootLayout from "@jansora/ui/esm/components/enhanced/layout/RootLayout";
import Header from "@jansora/ui/esm/components/enhanced/layout/header";
import HeaderLeft from "@/components/header/HeaderLeft";
import {loadConfig} from "@/lib/config/initConfig";
import {hash} from "@/lib/util/utils";
import {CategoryCache} from "@/lib/declares/store";

import PaddedFullPageLayout from "@jansora/ui/esm/components/enhanced/layout/PaddedFullPageLayout";
import HeaderRight from "@/components/header/HeaderRight";


export default function Layout({children}) {

    // const data = initialData();

    return (
        <RootLayout>
            {/*<GlobalStoreProvider initialData={data}>*/}
            <Header
                left={<HeaderLeft />}
                right={<HeaderRight />}
            />
                <PaddedFullPageLayout>

                    {children}

                </PaddedFullPageLayout>
            {/*</GlobalStoreProvider>*/}
        </RootLayout>
    )
}

const initialData = () => {

    const config = loadConfig();

    const data: { categories: CategoryCache[]} = {

        categories: config.categories.map(category => {
            return {
                key: hash(category.path),
                value: category.name,
                books: category.books.map(book => {
                    return {
                        key: hash(book.path),
                        value: book.name,
                    }
                }),
            }
        })
    }


    return data
}

