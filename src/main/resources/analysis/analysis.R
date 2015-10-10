library("ggplot2")

results <- read.csv("../diophantine.csv")
results <- data.matrix(results)

# To avoid performing equality checks on floating point values, scale all results up to the > 1 range
results <- results * 100

x <- seq(20, 49, 2)
y <- seq(10, 49, 2)

fun <- function(x, y) {
    value <- results[((results[,1]<x+1) & (results[,1]>x-1)) & ((results[,2]<y+1) & (results[,2]>y-1))][3]
    value <- if (is.na(value)) 0 else value
    # Scale value back down to original
    value / 100
}
VecFun <- Vectorize(fun)
color <- rgb(180,160,100, maxColorValue=255)

png('genetic_large.png', bg="transparent", width=600, height=600)
plot <- persp(x = x, y = y, z = outer(x, y, VecFun), phi = 25, theta = 45, ticktype="detailed", col=color, shade=.05,
      xlab = "Mutation Factor (%)", ylab = "Survival Rate (%)", zlab = "Search Time (ms)"
)
plot
dev.off()