<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>

    <style type="text/css">

        body {
            font-family: 'Roboto', sans-serif;
            font-size: 14px;
        }

        p {
            clear: left;
        }

        table {
            border-collapse: collapse;
            width: 600px;
            clear: left;
        }

        td, th {
            border: 1px solid #ddd;
            padding: 8px;
        }

        th {
            padding-top: 12px;
            padding-bottom: 12px;
            text-align: left;
            background-color: #484D6D;
            color: white;
        }

    </style>

    <title>
        Daycare catering bill.
    </title>

</head>

<body>

<p>
    <#if correction == false>
        This is the ${month} catering bill for ${childName}. <br>
    <#else>
        This is a correction for the ${month} catering bill for ${childName}. <br>
        Please disregard any previous versions of this month's catering bill. <br>
    </#if>
    <br>
    You may find details regarding specific catering orders in the table below. <br>
    Please feel free to contact us in case you have any questions on concerns.
</p>

<table>

    <tr>
        <th>Date</th>
        <th>Catering option</th>
        <th>Price</th>
    </tr>

    <#list dailyOrders as order>
        <tr>
            <td>${order.orderDate}</td>
            <td>${order.cateringOptionName}</td>
            <#setting locale="pl_PL">
            <td>${order.cateringOptionPrice?string.currency}</td>
        </tr>
    </#list>

    <#if dailyOrders?size == 0>
        <tr style="text-align: center">
            <td colspan="3">No catering orders in this month.</td>
        </tr>
    </#if>

    <tr style="font-weight: bold">
        <td colspan="2" style="text-align: center">Total due:</td>
        <#setting locale="pl_PL">
        <td>${totalDue?string.currency}</td>
    </tr>

</table>

<p>
    You may find our bank account details below:<Br>
    XXXXXXXXXXXXXXXXXX<br>
    XXXXXXXXXXXXXXXXXX
</p>

<p>
    Kind Regards, <br>
    Daycare XXX
</p>

</body>
</html>