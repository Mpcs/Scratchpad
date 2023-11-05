rootProject.name = "Scratchpad"
include("Config")
include("Config:AnnotationProcessor")
findProject(":Config:AnnotationProcessor")?.name = "AnnotationProcessor"
include("Logging")
include("Utility")
