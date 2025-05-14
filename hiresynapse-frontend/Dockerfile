FROM node:18-slim AS builder
WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

FROM node:18-slim AS production
WORKDIR /app

RUN npm install -g serve

COPY --from=builder /app/build /app/build

EXPOSE 3020

ENV PORT=3020

CMD ["serve", "-s", "build", "-l", "3020"]
