
# 🎨 Webcam-Based Painting (RegionFinder UI)

A full-stack Java + React web application that allows users to upload an image and recolor or paint regions based on color similarity.  
The backend runs a **Spring Boot** image-processing service built around the `RegionFinder` algorithm, while the frontend provides an intuitive UI built with **React + Vite**.

https://webcam-based-painting-0hdv.onrender.com/
---

## 🚀 Features

- 🖼️ Upload any image (PNG or JPEG)
- 🎯 Select target and paint colors
- ⚙️ Adjustable parameters (`maxColorDiff`, `minRegionSize`)
- 🎨 Modes: Recolor Regions or Paint Largest Region
- ⚡ Server-backed image processing
- 🧩 Deployable on Render.com

---

## 🧠 Architecture Overview
java-frontend → React + Vite UI

java-backend-spring → Spring Boot backend

---

## 🧰 Local Development

### 🖥️ Backend

```bash
cd java-backend-spring
mvn clean package
mvn spring-boot:run
```

### 🖥️ Frontkend
```bash
cd java-frontend-rf
npm install
npm run dev
```
### ✨Contributors

Godwin Kangor — Developer
Based on Dartmouth CS10 (Winter 2024) RegionFinder project.
