## Inspiration  
- **JellyCar** inspired the development of our soft-body physics engine.  
- Our collective childhood fascination with space shaped the theme and design.  

## What It Does  
- The soft-body physics engine interacts with the phone's gyroscope, allowing users to explore planets dynamically.  
- All planets and backgrounds are procedurally generated, minimizing storage and memory usage for optimal performance.  

## How We Built It  
- Developed using **LibGDX**, leveraging its Java-based graphics utilities for seamless rendering.  
- **OpenGL** is used for drawing planets, while **GLSL shaders** generate procedural textures using Perlin noise for a unique visual experience.  
- Hosted the Android APK available for download using C# and .NET framework

## Challenges We Faced  
- Precision differences between our Windows development environment and real Android hardware posed debugging challenges.  
- Achieving stability in the physics engine required extensive fine-tuning.  

## Accomplishments We're Proud Of  
- Successfully building a custom physics engine within a tight timeframe.  
- Overcoming Android hardware integration and compatibility hurdles.  

## What We Learned  
- The importance of accounting for hardware differences when developing physics simulations.  
- Optimizing procedural rendering techniques for mobile performance.  
- Effective debugging strategies for Android development.  

## What's Next for **GyroVerse**  
- Enhancing interactivity with other gyroscope based actions, such as resetting the planets
- Adding labels for the planets to enhance the educational aspect
- Hopefully preparing for a release on the Google Play Store
