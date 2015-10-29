
/*Working here.. need to find order total for each customers OrderNum. 
Maybe a temp table and maybe subquery.*/


/*Order information query*/
  SELECT customer.Street,
         customer.City,
         customer.State,
         customer.Zip,
         CustNum,
         OrderNum,
         OrderDate,
         SKU,
         Description,
         UnitPrice,
         OrdQty AS Quantity,
         UnitWeight,
         sum(OrdQty * UnitPrice) AS Total_Price,
         sum(OrdQty * UnitWeight) AS Total_Weight
   		 FROM invoice
         JOIN customer USING (CustNum)
         NATURAL JOIN invoicelineitem JOIN inventory USING (SKU)
GROUP BY OrderNum, CustNum, SKU;
    
/*Totals on Orders query*/
SELECT count(OrderNum) AS Total_Orders,
       sum(OrdQty * UnitPrice) AS Total_Sales,
       sum(OrdQty) AS Total_Items,
       sum(UnitWeight) AS Total_Weight
  FROM invoice NATURAL JOIN invoicelineitem JOIN inventory USING (SKU);