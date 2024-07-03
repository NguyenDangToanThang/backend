document.getElementById('add-feature').addEventListener('click', function () {
    const featureContainer = document.createElement('div');
    featureContainer.classList.add('feature');

    const select = document.createElement('select');
    select.classList.add('feature-select');
    select.classList.add('form-control');

    const nameInput = document.createElement("input");
    nameInput.type = 'text';
    nameInput.name = 'productFeatureName';
    nameInput.classList.add("form-control");
    nameInput.placeholder = "Enter name";

    featureContainer.appendChild(nameInput);

    const descInput = document.createElement('input');
    descInput.type = 'text';
    descInput.name = 'productFeatureDesc';
    descInput.classList.add('form-control')
    descInput.placeholder = 'Enter value';
    featureContainer.appendChild(descInput);


    const removeButton = document.createElement('button');
    removeButton.classList.add('remove-feature');
    removeButton.innerHTML = '<i class="fa-solid fa-minus"></i>';
    removeButton.addEventListener('click', function () {
        featureContainer.remove();
    });
    featureContainer.appendChild(removeButton);

    document.querySelector('.features-container').insertBefore(featureContainer, document.getElementById('add-feature'));
});

document.querySelectorAll('.remove-feature').forEach(button => {
    button.addEventListener('click', function () {
        button.parentElement.remove();
    });
});


document.addEventListener('DOMContentLoaded', () => {
    const buttons = document.querySelectorAll('.category-buttons button');

    buttons.forEach(button => {
        button.addEventListener('click', () => {
            buttons.forEach(btn => btn.classList.remove('active'));
            button.classList.add('active');
            document.getElementById('category_id').setAttribute("value",button.value);
        });
    });
});

document.addEventListener('DOMContentLoaded', () => {
    const buttons = document.querySelectorAll('.brand-buttons button');

    buttons.forEach(button => {
        button.addEventListener('click', () => {
            buttons.forEach(btn => btn.classList.remove('active'));
            button.classList.add('active');
            document.getElementById('brand_id').setAttribute("value",button.value);
        });
    });
});

document.getElementById('add-btn').addEventListener('click', () => {
    const features = [];
    for(const child of document.querySelector('.features-container').children){
        if(child.classList.contains('feature')){
            const feature = {};
        for(const input of child.children){
            if(input.name == 'productFeatureName'){
                feature.name = input.value;
            }
            if(input.name == 'productFeatureDesc'){
                feature.description = input.value;
            }
        }
            features.push(feature);
        }
    }
    document.getElementById('features-input').value = JSON.stringify(features);
})

const originalPriceInput = document.getElementById('originalPrice');
const discountInput = document.getElementById('discount');
const discountedPriceInput = document.getElementById('discountedPrice');
const defaultValue = 0.00;

        function calculateDiscountedPrice() {
          if (originalPriceInput.value !== '' && discountInput.value !== '') {
            const originalPrice = parseFloat(originalPriceInput.value);
            const discount = parseFloat(discountInput.value);

            const discountAmount = originalPrice * (discount / 100);
            const discountedPrice = originalPrice - discountAmount;

            discountedPriceInput.value = discountedPrice.toFixed(2);
          }
          else {
            discountedPriceInput.value = defaultValue.toFixed(2);
          }
        }

        originalPriceInput.addEventListener('input', calculateDiscountedPrice);
        discountInput.addEventListener('input', calculateDiscountedPrice);

        calculateDiscountedPrice();