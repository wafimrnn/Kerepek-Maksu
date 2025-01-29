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
    const formattedMethod = method.toUpperCase(); // Convert method to uppercase
    document.getElementById("CASH").style.display = formattedMethod === "CASH" ? "block" : "none";
    document.getElementById("QR").style.display = formattedMethod === "QR" ? "block" : "none";
    document.getElementById("payment-method").value = formattedMethod; // Set the uppercase value
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
    const saleDate = new Date().toISOString().split("T")[0];

    if (totalAmount > 0) {
        const orderItems = [];
        document.querySelectorAll("#order-items tr").forEach((row) => {
            const prodId = parseInt(row.getAttribute("data-product-id"));
            const qty = parseInt(row.querySelector(".qty input").value);
            const subtotal = parseFloat(row.querySelector(".subtotal").textContent.replace("RM ", ""));
            orderItems.push({ prodId, qty, subtotal });
        });

        // Ensure orderItems is not empty
        if (orderItems.length === 0) {
            alert("No items in the order!");
            return;
        }

        const requestData = {
            totalAmount: totalAmount,
            paymentMethod: paymentMethod,
            saleDate: saleDate,
            orderItems: orderItems,
        };

        // Send POST request
        fetch("/KedaiKerepekMaksu/completeSale", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(requestData),
        })
            .then((response) => {
                if (response.ok) return response.json();
                else throw new Error("Failed to complete the order.");
            })
            .then((data) => {
                alert("Order Completed Successfully!");
                // Reset UI
                document.getElementById("order-items").innerHTML = "";
                updateTotals();
            })
            .catch((error) => {
                alert("Error: " + error.message);
            });
    } else {
        alert("No items in the order to complete!");
    }
}