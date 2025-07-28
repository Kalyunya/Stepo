#include <WiFi.h>
#include <WebServer.h>
#include <DHT.h>
#include <U8g2lib.h>

#define DHTPIN 5
#define DHTTYPE DHT11

const char* ssid = "Green";
const char* password = "1qa2wsde3fr4";

// const char* ssid = "MERCUSYS_0499";
// const char* password = "0989783459";

DHT dht(DHTPIN, DHTTYPE);
WebServer server(80);

// OLED дисплей
U8G2_SSD1306_128X32_UNIVISION_F_HW_I2C u8g2(U8G2_R0, /* clock=*/22, /* data=*/21);

// Змінна для імені
String fullName = "user";

// ==== Обробка запиту на /data ====
void handleData() {
  float temp = dht.readTemperature();
  float hum = dht.readHumidity();

  if (isnan(temp) || isnan(hum)) {
    server.send(500, "application/json", "{\"error\":\"Sensor error\"}");
    return;
  }

  String json = "{";
  json += "\"temperature\":" + String(temp, 1) + ",";
  json += "\"humidity\":" + String(hum, 1);
  json += "}";

  server.send(200, "application/json", json);
}

// ==== Обробка запиту на /name (POST) ====
void handleName() {
  if (server.hasArg("plain")) {
    String body = server.arg("plain");
    fullName = body;
    Serial.println("Hello: " + fullName);
    server.send(200, "text/plain", "OK");
  } else {
    server.send(400, "text/plain", "No data received");
  }
}

// ==== OLED Вивід ====
void displayOLED() {
  u8g2.clearBuffer();
  u8g2.setFont(u8g2_font_ncenB08_tr);
  u8g2.drawStr(0, 12, "Stepo");
  u8g2.drawUTF8(0, 28, fullName.c_str()); // Підтримка UTF-8
  u8g2.sendBuffer();
}

// ==== Setup ====
void setup() {
  Serial.begin(115200);
  dht.begin();
  u8g2.begin();

  WiFi.begin(ssid, password);
  Serial.print("Підключення до WiFi");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("\nПідключено!");
  Serial.print("IP адреса: ");
  Serial.println(WiFi.localIP());

  server.on("/data", handleData);
  server.on("/name", HTTP_POST, handleName); // ← Новий маршрут
  server.begin();
  Serial.println("Сервер запущено.");
}

// ==== Loop ====
void loop() {
  server.handleClient();

  displayOLED();  // Постійно оновлюємо OLED

  float temperature = dht.readTemperature();
  float humidity = dht.readHumidity();

  if (!isnan(temperature) && !isnan(humidity)) {
    Serial.print("Температура: ");
    Serial.print(temperature, 1);
    Serial.print(" °C   |   Вологість: ");
    Serial.print(humidity, 1);
    Serial.println(" %");
  } else {
    Serial.println("Помилка зчитування з датчика!");
  }

  delay(2000);
}
