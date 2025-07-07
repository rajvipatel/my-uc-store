// Cart functionality JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Initialize cart functionality
    initializeCart();
    initializeCheckout();
});

function initializeCart() {
    // Update quantity buttons
    const quantityInputs = document.querySelectorAll('.quantity-input');
    quantityInputs.forEach(input => {
        input.addEventListener('change', function() {
            const bookId = this.dataset.bookId;
            const quantity = parseInt(this.value);
            
            if (quantity < 1) {
                this.value = 1;
                return;
            }
            
            updateCartItem(bookId, quantity);
        });
    });

    // Remove item buttons
    const removeButtons = document.querySelectorAll('.remove-item-btn');
    removeButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const bookId = this.dataset.bookId;
            const bookTitle = this.dataset.bookTitle;
            
            if (confirm(`Are you sure you want to remove "${bookTitle}" from your cart?`)) {
                removeCartItem(bookId);
            }
        });
    });

    // Add to cart buttons
    const addToCartButtons = document.querySelectorAll('.add-to-cart-btn');
    addToCartButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const bookId = this.dataset.bookId;
            const bookTitle = this.dataset.bookTitle;
            
            // Show loading state
            const originalText = this.innerHTML;
            this.innerHTML = '<span class="loading"></span> Adding...';
            this.disabled = true;
            
            // Submit form
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '/cart/add';
            
            const bookIdInput = document.createElement('input');
            bookIdInput.type = 'hidden';
            bookIdInput.name = 'bookId';
            bookIdInput.value = bookId;
            
            const quantityInput = document.createElement('input');
            quantityInput.type = 'hidden';
            quantityInput.name = 'quantity';
            quantityInput.value = '1';
            
            form.appendChild(bookIdInput);
            form.appendChild(quantityInput);
            document.body.appendChild(form);
            form.submit();
        });
    });
}

function updateCartItem(bookId, quantity) {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/cart/update';
    
    const bookIdInput = document.createElement('input');
    bookIdInput.type = 'hidden';
    bookIdInput.name = 'bookId';
    bookIdInput.value = bookId;
    
    const quantityInput = document.createElement('input');
    quantityInput.type = 'hidden';
    quantityInput.name = 'quantity';
    quantityInput.value = quantity;
    
    form.appendChild(bookIdInput);
    form.appendChild(quantityInput);
    document.body.appendChild(form);
    form.submit();
}

function removeCartItem(bookId) {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/cart/remove';
    
    const bookIdInput = document.createElement('input');
    bookIdInput.type = 'hidden';
    bookIdInput.name = 'bookId';
    bookIdInput.value = bookId;
    
    form.appendChild(bookIdInput);
    document.body.appendChild(form);
    form.submit();
}

function initializeCheckout() {
    const checkoutForm = document.getElementById('checkout-form');
    if (checkoutForm) {
        checkoutForm.addEventListener('submit', function(e) {
            const submitButton = this.querySelector('button[type="submit"]');
            if (submitButton) {
                // Show loading state
                const originalText = submitButton.innerHTML;
                submitButton.innerHTML = '<span class="loading"></span> Processing Payment...';
                submitButton.disabled = true;
                
                // Re-enable button after 10 seconds as fallback
                setTimeout(() => {
                    submitButton.innerHTML = originalText;
                    submitButton.disabled = false;
                }, 10000);
            }
        });
    }
}

// Search functionality
function initializeSearch() {
    const searchForm = document.querySelector('.search-form');
    if (searchForm) {
        const searchInput = searchForm.querySelector('input[name="query"]');
        
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                if (this.value.trim()) {
                    searchForm.submit();
                }
            }
        });
    }
}

// Format currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    }).format(amount);
}

// Show notification
function showNotification(message, type = 'success') {
    const notification = document.createElement('div');
    notification.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
    notification.style.top = '20px';
    notification.style.right = '20px';
    notification.style.zIndex = '9999';
    notification.style.minWidth = '300px';
    
    notification.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    document.body.appendChild(notification);
    
    // Auto-remove after 5 seconds
    setTimeout(() => {
        if (notification.parentNode) {
            notification.remove();
        }
    }, 5000);
}

// Smooth scroll to top
function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}

// Initialize search when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    initializeSearch();
});
