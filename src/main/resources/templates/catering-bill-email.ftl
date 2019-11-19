<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Sending Email with Freemarker HTML Template Example</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>

    <!-- use the font -->
    <style>
        body {
            font-family: 'Roboto', sans-serif;
            font-size: 48px;
        }
    </style>
</head>
<body style="margin: 0; padding: 0;">

<p>Below you may find ${subject}.</p>

<table align="left" border="1" cellpadding="0" cellspacing="0" width="600" style="border-collapse: collapse;">
    <tr>
        <th>Date</th>
        <th>Catering option</th>
        <th>Price</th>
    </tr>
    <#list dailyOrders as order>
        <tr>
            <td>${order.orderDate}</td>
            <td>${order.cateringOptionName}</td>
            <td>${order.cateringOptionPrice} PLN</td>
        </tr>
    </#list>
    <tr style="font-weight: bold">
        <td colspan="2" style="text-align: center">Total due:</td>
        <td>${totalDue} PLN</td>
    </tr>
</table>

<p style="clear:left">
    Best Regards, <br>
    daycare xxx
</p>

</body>
</html>