.PHONY: run
run:
	@echo "Starting Ink IDE"
	@./gradlew run

.PHONY: test
test:
	@echo "Ink IDE Code Tests"
	@./gradlew test