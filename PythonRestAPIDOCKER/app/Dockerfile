FROM python:3.6

WORKDIR /app

COPY . /app
RUN pip install -r requirements.txt

EXPOSE 5000
ENV FLASK_APP rest.py
CMD python rest.py runserver -h 0.0.0.0
