FROM python:3.8

WORKDIR /usr/src/app


RUN apt-get update && apt-get install -y netcat
RUN python3 -m pip install --upgrade pip
COPY requirements.txt ./
RUN python3 -m pip install --no-cache-dir -r requirements.txt

COPY . .

CMD  python3 -m flask run --host=0.0.0.0 --port=${PORT}