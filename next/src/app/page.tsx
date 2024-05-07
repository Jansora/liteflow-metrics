import React from "react";
import TraceDemo from "@/app/TraceDemo";

export function clientHeader() {

    return {
        'Content-Type': 'application/json',
        'x-authenticated-userid': 'abc'
    }
}

export default async function Page() {


    // const books = initBooks(config.categories[0])

    // const response = await fetch("https://x6.antv.antgroup.com/data/bpmn.json", {
    //     // method: 'PUT',
    //     headers: clientHeader(),
    //     // body: JSON.stringify({
    //     //     "code": "",
    //     //     "data": "{\"BizCode\":\"1003\",\"BizId\":\"FU2020112324922\",\"AptitudesCode\":\"BID2022102064284\",\"voucherBaseInfo\":{\"VoucherId\":\"01b77c5a-6c6d-4ff6-9701-acd52ce5f131\",\"VoucherTypeId\":\"32310D48-1CD6-4044-82DD-BD92BABD74D5\",\"State\":\"40\",\"CreationDate\":\"2023-05-29\",\"ExpenseCategoryId\":\"885f8d88-8462-4107-a3d4-0a3a3fb31160\",\"Amount\":\"24345\",\"IsOverCostcenter\":null,\"OverSupervisorPersonId\":null},\"empInfo\":{\"LastName\":\"周燕\",\"EmployeeNumber\":\"14862\",\"ApplyerPersonId\":\"80351\",\"CreateBy\":\"80351\"},\"depatInfo\":{\"CompanyId\":\"110\",\"CompanyName\":\"亚信南京\",\"SbuId\":\"109\",\"CostcenterId\":\"16463\",\"RegionId\":\"08\",\"RegionName\":\"南京\"},\"paymtInfo\":{\"CurrencyType\":\"CNY\",\"PaymentType\":\"CASHCARD\",\"VendorId\":null,\"PayableTo\":\"***\",\"BankProvince\":\"***\",\"BankCity\":\"***\",\"BankSub\":\"***\",\"BankAccount\":\"***\",\"BankType\":\"***\"},\"voucherItemsInfo\":[{\"ExpenseTypeId\":\"8ef8b058-3047-4b8b-a2fc-6350a4ef8286\",\"ItemAmount\":\"24345\",\"OuccerDate\":\"2023-05-01\",\"Comments\":\"2022-2023年中国联通软研院公众应用（认证、监控及合作等应用）系统应用维护服务（招标编号：2022CUC-RJY-PT0811）;应标流水号:BID2022102064284\",\"Currency\":\"CNY\",\"InvoiceNo\":\"37336004\",\"AmountCost\":\"22966.98\",\"AmountTax\":\"1378.02\",\"Rate\":\"1\",\"BudgetProjNumber\":null}],\"voucherDepositItemsInfo\":[null],\"voucherProcessInfo\":[{\"ApproverPersonId\":\"80351\",\"ApproveDate\":\"2023-05-29\",\"ApproverOrderNo\":\"1\",\"ApproveReason\":\"申请人提交\",\"ApproverName\":\"周燕\"}],\"writtenOffLoanInfo\":null}"
    //     // })
    //
    // });

    const response = await fetch("http://127.0.0.1:8080/ai-chameleon-runtime/test", {
        method: 'PUT',
        headers: clientHeader(),
        cache: 'no-store',
        body: JSON.stringify({
            "code": "UUID-1234567890",
            "data": "{\"BizCode\":\"1003\",\"writtenOffLoanInfo\":null}"
        })

    });
    const data = await response.json();

    return (
        <>

            <TraceDemo data={data}/>

        </>
    )
}
