FROM python:3.8

WORKDIR /app
RUN python3 -m pip install --upgrade pip
COPY requirements.txt requirements.txt
RUN python3 -m pip install -r requirements.txt

COPY initialize.py initialize.py
COPY addresses.py addresses.py
COPY main.py main.py
COPY create_internal_default_product_laisse.py create_internal_default_product_laisse.py
COPY create_small_partner_caouthouc_product.py create_small_partner_caouthouc_product.py
COPY create_big_partner_medium_maxi_adult_product.py create_big_partner_medium_maxi_adult_product.py

COPY start.sh start.sh

RUN chmod +x start.sh

ENTRYPOINT [ "./start.sh" ]