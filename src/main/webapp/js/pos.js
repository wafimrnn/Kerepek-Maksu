// Event Listener for Add to Order buttons
document.querySelectorAll(".add-to-order").forEach(button => {
    button.addEventListener("click", function() {
        const prodId = this.getAttribute("data-prodId");
        const prodName = this.getAttribute("data-prodName");
        const prodPrice = parseFloat(this.getAttribute("data-prodPrice"));

        addToOrder(prodName, prodPrice, prodId);
    });
});

// Function to toggle between payment methods
function togglePayment(method) {
    document.getElementById("cash").style.display = method === "cash" ? "block" : "none";
    document.getElementById("qr").style.display = method === "qr" ? "block" : "none";
    document.getElementById("payment-method").value = method;
}

// Function to add items to the order
function addToOrder(productName, productPrice, prodId) {
    const orderItems = document.getElementById("order-items");
    const existingRow = document.querySelector(`[data-product-name="${productName}"]`);

    if (existingRow) {
        // If the product already exists, update the quantity and subtotal
        const qtyInput = existingRow.querySelector(".qty input");
        const subtotalCell = existingRow.querySelector(".subtotal");

        const newQty = parseInt(qtyInput.value) + 1; // Increase quantity by 1
        qtyInput.value = newQty; // Update the quantity input field
        subtotalCell.textContent = `RM ${(newQty * productPrice).toFixed(2)}`; // Update subtotal
    } else {
        // If the product doesn't exist, create a new row
        const row = document.createElement("tr");
        row.setAttribute("data-product-name", productName);
        row.setAttribute("data-product-id", prodId);

        row.innerHTML = `
            <td>${productName}</td>
            <td class="qty">
                <input type="number" min="1" value="1" onchange="updateRowSubtotal(this, ${productPrice})">
            </td>
            <td class="subtotal">RM ${productPrice.toFixed(2)}</td>
            <td><button onclick="removeItem(this)">Remove</button></td>
        `;

        orderItems.appendChild(row); // Append the new row to the table
    }

    updateTotals(); // Recalculate totals
}

function updateRowSubtotal(input, productPrice) {
    const newQty = parseInt(input.value) || 1; // Default to 1 if input is invalid
    input.value = newQty; // Ensure the input value is valid
    const row = input.closest("tr");
    const subtotalCell = row.querySelector(".subtotal");
    subtotalCell.textContent = `RM ${(newQty * productPrice).toFixed(2)}`;

    updateTotals(); // Recalculate the overall totals
}

// Function to update totals
function updateTotals() {
    let subtotal = 0;
    document.querySelectorAll("#order-items tr").forEach((row) => {
        const subtotalText = row.querySelector(".subtotal").textContent;
        subtotal += parseFloat(subtotalText.replace("RM ", ""));
    });

    const total = subtotal;
    document.getElementById("subtotal").textContent = `RM ${subtotal.toFixed(2)}`;
    document.getElementById("total").textContent = `RM ${total.toFixed(2)}`;
    document.getElementById("total-amount").value = total;
}

// Function to remove items from the order
function removeItem(button) {
    const row = button.closest("tr");
    row.remove();
    updateTotals();
}

// Function to calculate change for cash payment
function calculateChange() {
    const moneyReceived = parseFloat(document.getElementById("money-received").value) || 0;
    const totalAmount = parseFloat(document.getElementById("total-amount").value) || 0;
    const change = moneyReceived - totalAmount;

    document.getElementById("change").value = change >= 0 ? `RM ${change.toFixed(2)}` : "Insufficient";
}

// Function to handle order completion
function completeOrder() {
    const totalAmount = parseFloat(document.getElementById("total-amount").value) || 0;
    const paymentMethod = document.getElementById("payment-method").value;
    const userId = 5; // Replace with actual logic to get the logged-in user's ID
    const saleDate = new Date().toISOString().split("T")[0]; // Current date in yyyy-MM-dd format

    if (totalAmount > 0) {
        const orderItems = [];
        document.querySelectorAll("#order-items tr").forEach((row) => {
            const prodId = parseInt(row.getAttribute("data-product-id")); // Get prodId from data attribute
            const qty = parseInt(row.querySelector(".qty input").value); // Get the input value for quantity
            const subtotal = parseFloat(row.querySelector(".subtotal").textContent.replace("RM ", ""));
            orderItems.push({ prodId, qty, subtotal }); // Use prodId in the order item
        });

        // Prepare data to send
        const requestData = {
            totalAmount: totalAmount,
            paymentMethod: paymentMethod,
            userId: userId,
            saleDate: saleDate,
            orderItems: orderItems,
        };

        // Send POST request to the backend
        fetch("http://localhost:8088/KedaiKerepekMaksu/CompleteSaleServlet", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(requestData),
        })
            .then((response) => {
                if (response.ok) {
                    return response.json(); // Assuming backend returns JSON
                } else {
                    throw new Error("Failed to complete the order.");
                }
            })
            .then((data) => {
                // Handle successful response
                const responseMessage = document.getElementById("response-message");
                responseMessage.style.display = "block";
                responseMessage.textContent = "Order Completed Successfully!";
                responseMessage.style.color = "green";

                // Clear the order items and reset totals
                document.getElementById("order-items").innerHTML = "";
                document.getElementById("subtotal").textContent = "RM 0";
                document.getElementById("total").textContent = "RM 0";
                document.getElementById("total-amount").value = 0;
                document.getElementById("money-received").value = "";
                document.getElementById("change").value = "";
            })
            .catch((error) => {
                // Handle errors
                const responseMessage = document.getElementById("response-message");
                responseMessage.style.display = "block";
                responseMessage.textContent = "Error: " + error.message;
                responseMessage.style.color = "red";
            });
    } else {
        // Show error if no items in the order
        const responseMessage = document.getElementById("response-message");
        responseMessage.style.display = "block";
        responseMessage.textContent = "No items in the order to complete!";
        responseMessage.style.color = "red";
    }
}
